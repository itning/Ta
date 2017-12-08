package top.itning.ta.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import top.itning.ta.dao.ClazzDao;
import top.itning.ta.dao.StudentInfoDao;
import top.itning.ta.dao.StudentLeaveDao;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.exception.DataNotFindException;
import top.itning.ta.exception.NullParameterException;
import top.itning.ta.service.StudentInfoService;

import javax.servlet.ServletOutputStream;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 学生信息服务实现类
 *
 * @author wangn
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class StudentInfoServiceImpl implements StudentInfoService {
    /**
     * xlsx mime type
     */
    private static final String MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    /**
     * xls mime type
     */
    private static final String MIME_XLS = "application/vnd.ms-excel";

    @Value("${spring.http.multipart.location}")
    private String uploadPath;

    private final StudentInfoDao studentInfoDao;

    private final ClazzDao clazzDao;

    private final StudentLeaveDao studentLeaveDao;

    @Autowired
    public StudentInfoServiceImpl(StudentInfoDao studentInfoDao, ClazzDao clazzDao, StudentLeaveDao studentLeaveDao) {
        this.studentInfoDao = studentInfoDao;
        this.clazzDao = clazzDao;
        this.studentLeaveDao = studentLeaveDao;
    }

    @Override
    public List<StudentInfo> getAllStudentInfoByClass(String clazz) throws DataNotFindException {
        Clazz clazzInfo = clazzDao.findOne(clazz);
        if (clazzInfo == null) {
            throw new DataNotFindException("班级ID:" + clazz + "没有找到");
        }
        return studentInfoDao.findAllByClazz(clazzInfo);
    }

    @Override
    public StudentInfo getOneStudentInfoByID(String id) throws DataNotFindException {
        if (!studentInfoDao.exists(id)) {
            throw new DataNotFindException("ID:" + id + "的学生没有找到");
        }
        return studentInfoDao.getOne(id);
    }

    @Override
    public Clazz getClassInfoByID(String id) throws DataNotFindException {
        Clazz clazz = clazzDao.findOne(id);
        if (clazz == null) {
            throw new DataNotFindException("班级ID:" + id + "没有找到");
        }
        return clazz;
    }

    @Override
    public void addStudentInfo(StudentInfo studentInfo, MultipartFile file) throws NullParameterException {
        if (studentInfo.getAddress() == null) {
            throw new NullParameterException("参数address为空");
        }
        if (studentInfo.getBirthday() == null) {
            throw new NullParameterException("参数birthday为空");
        }
        if (studentInfo.getCollege() == null) {
            throw new NullParameterException("参数college为空");
        }
        if (studentInfo.getHtel() == null) {
            throw new NullParameterException("参数htel为空");
        }
        if (studentInfo.getTel() == null) {
            throw new NullParameterException("参数tel为空");
        }
        if (studentInfo.getId() == null) {
            throw new NullParameterException("参数id为空");
        }
        if (studentInfo.getName() == null) {
            throw new NullParameterException("参数name为空");
        }
        if (studentInfo.getProfession() == null) {
            throw new NullParameterException("参数profession为空");
        }
        if (studentInfo.getTeacher() == null) {
            throw new NullParameterException("参数teacher为空");
        }
        if (studentInfo.getIntime() == null) {
            throw new NullParameterException("参数intime为空");
        }
        //有上传文件
        if (!file.isEmpty()) {
            //获取文件扩展名
            String extensionName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            //用学号作为文件名
            String newFileName = studentInfo.getId() + extensionName;
            studentInfo.setImg(newFileName);
            File newFile = new File(uploadPath + newFileName);
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                //回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } else {
            if (studentInfoDao.exists(studentInfo.getId())) {
                String img = studentInfoDao.findimgByID(studentInfo.getId());
                if (img != null) {
                    studentInfo.setImg(img);
                }
            }
        }
        studentInfoDao.saveAndFlush(studentInfo);
    }

    @Override
    public void delStudentInfo(String id) throws DataNotFindException {
        if (!studentInfoDao.exists(id)) {
            throw new DataNotFindException("ID:" + id + "不存在");
        }
        //先删除请假信息
        studentLeaveDao.deleteAllBySid(studentInfoDao.getOne(id));
        studentInfoDao.delete(id);
    }

    @Override
    public void downStudentInfo(ServletOutputStream servletOutputStream, String... id) throws DataNotFindException, IOException {
        for (String s : id) {
            if (!studentInfoDao.exists(s)) {
                throw new DataNotFindException("学生ID:" + s + "没有找到");
            }
        }
        Workbook workbook = new XSSFWorkbook();
        //创建工作簿 名:专业+班级
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        List<String> titleList = new ArrayList<>();
        titleList.add("学号");
        titleList.add("姓名");
        titleList.add("性别");
        titleList.add("出生日期");
        titleList.add("联系电话");
        titleList.add("家长电话");
        titleList.add("入学时间");
        titleList.add("是否在籍");
        titleList.add("班内职务");
        titleList.add("班主任");
        titleList.add("班级");
        titleList.add("家庭地址");
        titleList.add("学院");
        titleList.add("专业");
        titleList.add("备注");
        final int[] nowCell = {0};
        titleList.forEach(s -> {
            Cell cell = row.createCell(nowCell[0]++);
            cell.setCellValue(s);
        });
        nowCell[0] = 1;
        for (String s : id) {
            StudentInfo studentInfo = studentInfoDao.findOne(s);
            Row dataRow = sheet.createRow(nowCell[0]++);
            Cell idcell = dataRow.createCell(0);
            idcell.setCellValue(studentInfo.getId());

            Cell namecell = dataRow.createCell(1);
            namecell.setCellValue(studentInfo.getName());

            Cell sexcell = dataRow.createCell(2);
            sexcell.setCellValue(studentInfo.isSex() ? "男" : "女");

            Cell birthdaycell = dataRow.createCell(3);
            birthdaycell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(studentInfo.getBirthday()));

            Cell telcell = dataRow.createCell(4);
            telcell.setCellValue(studentInfo.getTel());

            Cell htelcell = dataRow.createCell(5);
            htelcell.setCellValue(studentInfo.getHtel());

            Cell intimecell = dataRow.createCell(6);
            intimecell.setCellValue(new SimpleDateFormat("yyyy-MM").format(studentInfo.getIntime()));

            Cell incell = dataRow.createCell(7);
            incell.setCellValue(studentInfo.isIsin() ? "是" : "否");

            Cell positioncell = dataRow.createCell(8);
            positioncell.setCellValue(studentInfo.getPosition());

            Cell teachercell = dataRow.createCell(9);
            teachercell.setCellValue(studentInfo.getTeacher());

            Cell classcell = dataRow.createCell(10);
            classcell.setCellValue(studentInfo.getClazz().getClazz());

            Cell addresscell = dataRow.createCell(11);
            addresscell.setCellValue(studentInfo.getAddress());

            Cell collegecell = dataRow.createCell(12);
            collegecell.setCellValue(studentInfo.getCollege());

            Cell professioncell = dataRow.createCell(13);
            professioncell.setCellValue(studentInfo.getProfession());

            Cell remarkscell = dataRow.createCell(13);
            remarkscell.setCellValue(studentInfo.getRemarks());
        }
        workbook.write(servletOutputStream);
        workbook.close();
    }

    @Override
    public void addStudentInfoByExcel(MultipartFile file) throws NullParameterException, DataNotFindException, IOException {
        if (file.isEmpty()) {
            throw new NullParameterException("file参数为空");
        }
        String contentType = file.getContentType();
        Workbook workbook;
        if (MIME_XLSX.equals(contentType)) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (MIME_XLS.equals(contentType)) {
            workbook = new HSSFWorkbook(file.getInputStream());
        } else {
            throw new IllegalArgumentException("文件类型不正确:" + contentType);
        }
        Sheet sheetAt = workbook.getSheetAt(0);
        List<StudentInfo> studentInfoList = new ArrayList<>();
        for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {
            Row row = sheetAt.getRow(i);
            String id = getCellValue(row.getCell(0));
            String name = getCellValue(row.getCell(1));
            String sex = getCellValue(row.getCell(2));
            String birthday = getCellValue(row.getCell(3));
            String tel = getCellValue(row.getCell(4));
            String htel = getCellValue(row.getCell(5));
            String intime = getCellValue(row.getCell(6));
            String isin = getCellValue(row.getCell(7));
            String position = getCellValue(row.getCell(8));
            String teacher = getCellValue(row.getCell(9));
            String clazz = getCellValue(row.getCell(10));
            String address = getCellValue(row.getCell(11));
            String college = getCellValue(row.getCell(12));
            String profession = getCellValue(row.getCell(13));
            String remarks = getCellValue(row.getCell(14));
            //检查非空字段
            if (StringUtils.isAnyEmpty(id, name, sex, birthday, tel, htel, intime, isin, teacher, clazz, address, college, profession)) {
                continue;
            }
            List<Clazz> clazzList = clazzDao.findByClazz(clazz);
            if (clazzList.size() == 0) {
                continue;
            }
            StudentInfo studentInfo = new StudentInfo();
            try {
                Date birthdayDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                Date inttimeDate = new SimpleDateFormat("yyyy-MM").parse(intime);
                studentInfo.setBirthday(birthdayDate);
                studentInfo.setIntime(inttimeDate);
            } catch (ParseException e) {
                continue;
            }
            if (position != null) {
                studentInfo.setPosition(position);
            }
            if (remarks != null) {
                studentInfo.setRemarks(remarks);
            }

            studentInfo.setAddress(address);
            studentInfo.setClazz(clazzList.get(0));
            studentInfo.setCollege(college);
            studentInfo.setHtel(htel);
            studentInfo.setTel(tel);
            studentInfo.setTeacher(teacher);
            studentInfo.setProfession(profession);
            studentInfo.setId(id);
            studentInfo.setName(name);
            System.out.println(studentInfo);
            studentInfoList.add(studentInfo);
        }
        studentInfoList.forEach(studentInfoDao::saveAndFlush);
    }

    private String getCellValue(Cell cell) {
        String stringCellValue = null;
        try {
            stringCellValue = cell.getStringCellValue();
        } catch (NullPointerException e) {
            e.getMessage();
        }
        return stringCellValue;
    }
}
