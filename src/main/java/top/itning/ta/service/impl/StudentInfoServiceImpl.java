package top.itning.ta.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(StudentInfoServiceImpl.class);
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
            logger.warn("getAllStudentInfoByClass::班级ID:" + clazz + "没有找到");
            throw new DataNotFindException("班级ID:" + clazz + "没有找到");
        }
        return studentInfoDao.findAllByClazz(clazzInfo);
    }

    @Override
    public StudentInfo getOneStudentInfoByID(String id) throws DataNotFindException {
        if (!studentInfoDao.exists(id)) {
            logger.warn("getOneStudentInfoByID::ID:" + id + "的学生没有找到");
            throw new DataNotFindException("ID:" + id + "的学生没有找到");
        }
        return studentInfoDao.getOne(id);
    }

    @Override
    public Clazz getClassInfoByID(String id) throws DataNotFindException {
        Clazz clazz = clazzDao.findOne(id);
        if (clazz == null) {
            logger.warn("getClassInfoByID::班级ID:" + id + "没有找到");
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
            logger.debug("addStudentInfo::有上传文件");
            //获取文件扩展名
            String extensionName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            logger.debug("addStudentInfo::获取文件扩展名->" + extensionName);
            //用学号作为文件名
            String newFileName = studentInfo.getId() + extensionName;
            logger.debug("addStudentInfo::新文件名->" + newFileName);
            studentInfo.setImg(newFileName);
            File newFile = new File(uploadPath + newFileName);
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                logger.warn("addStudentInfo::文件写入本地失败->" + e.getMessage());
                //回滚
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        } else {
            logger.debug("addStudentInfo::无上传文件");
            if (studentInfoDao.exists(studentInfo.getId())) {
                logger.debug("addStudentInfo::学生ID->" + studentInfo.getId() + "存在");
                String img = studentInfoDao.findimgByID(studentInfo.getId());
                logger.debug("addStudentInfo::查找到的学生Img->" + img);
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
            logger.warn("delStudentInfo::ID:" + id + "不存在");
            throw new DataNotFindException("ID:" + id + "不存在");
        }
        //先删除请假信息
        logger.debug("delStudentInfo::开始删除请假信息->" + id);
        studentLeaveDao.deleteAllBySid(studentInfoDao.getOne(id));
        logger.debug("delStudentInfo::请假信息已删除,开始删除学生信息->" + id);
        studentInfoDao.delete(id);
        logger.debug("delStudentInfo::删除结束->" + id);
    }

    @Override
    public void downStudentInfo(ServletOutputStream servletOutputStream, String... id) throws DataNotFindException, IOException {
        for (String s : id) {
            if (!studentInfoDao.exists(s)) {
                logger.warn("downStudentInfo::学生ID:" + s + "没有找到");
                throw new DataNotFindException("学生ID:" + s + "没有找到");
            }
        }
        Workbook workbook = new XSSFWorkbook();
        //创建工作簿
        Sheet sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        logger.debug("downStudentInfo::准备标题数据");
        List<String> titleList = List.of("学号", "姓名", "性别", "出生日期", "联系电话", "家长电话", "入学时间", "是否在籍", "班内职务", "班主任", "班级", "家庭地址", "学院", "专业", "备注");
        logger.debug("downStudentInfo::标题数据集合大小->" + titleList.size());
        final int[] nowCell = {0};
        logger.debug("downStudentInfo::开始写入标题数据");
        titleList.forEach(s -> {
            Cell cell = row.createCell(nowCell[0]++);
            logger.debug("downStudentInfo::已创建Cell->" + (nowCell[0] - 1));
            cell.setCellValue(s);
            logger.debug("downStudentInfo::写入标题数据->" + s);
        });
        nowCell[0] = 1;
        for (String s : id) {
            StudentInfo studentInfo = studentInfoDao.findOne(s);
            Row dataRow = sheet.createRow(nowCell[0]++);
            logger.debug("downStudentInfo::已创建Cell->" + (nowCell[0] - 1));
            logger.debug("downStudentInfo::开始写入");
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
            logger.debug("downStudentInfo::结束写入");
        }
        workbook.write(servletOutputStream);
        logger.debug("downStudentInfo::workbook写入到输出流完成");
        workbook.close();
        logger.debug("downStudentInfo::workbook已关闭");
    }

    @Override
    public void addStudentInfoByExcel(MultipartFile file) throws NullParameterException, DataNotFindException, IOException {
        if (file.isEmpty()) {
            logger.warn("addStudentInfoByExcel::file为空");
            throw new NullParameterException("file参数为空");
        }
        String contentType = file.getContentType();
        logger.debug("addStudentInfoByExcel::获取到文件MIME类型->" + contentType);
        Workbook workbook;
        if (MIME_XLSX.equals(contentType)) {
            logger.debug("addStudentInfoByExcel::创建xlsx类型");
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (MIME_XLS.equals(contentType)) {
            logger.debug("addStudentInfoByExcel::创建xls类型");
            workbook = new HSSFWorkbook(file.getInputStream());
        } else {
            logger.warn("addStudentInfoByExcel::文件类型不正确:" + contentType);
            throw new IllegalArgumentException("文件类型不正确:" + contentType);
        }
        List<StudentInfo> studentInfoList = new ArrayList<>();
        logger.debug("addStudentInfoByExcel::获取到的工作表个数->" + workbook.getNumberOfSheets());
        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            logger.debug("addStudentInfoByExcel::获取第" + index + "个工作表");
            Sheet sheetAt = workbook.getSheetAt(index);
            logger.debug("addStudentInfoByExcel::获取到最后的行数->" + sheetAt.getLastRowNum());
            for (int i = 1; i <= sheetAt.getLastRowNum(); i++) {
                logger.debug("addStudentInfoByExcel::开始获取第" + i + "行");
                Row row = sheetAt.getRow(i);
                String id = getCellValue(row, 0);
                String name = getCellValue(row, 1);
                String sex = getCellValue(row, 2);
                String birthday = getCellValue(row, 3);
                String tel = getCellValue(row, 4);
                String htel = getCellValue(row, 5);
                String intime = getCellValue(row, 6);
                String isin = getCellValue(row, 7);
                String position = getCellValue(row, 8);
                String teacher = getCellValue(row, 9);
                String clazz = getCellValue(row, 10);
                String address = getCellValue(row, 11);
                String college = getCellValue(row, 12);
                String profession = getCellValue(row, 13);
                String remarks = getCellValue(row, 14);
                //检查非空字段
                if (StringUtils.isAnyEmpty(id, name, sex, birthday, tel, htel, intime, isin, teacher, clazz, address, college, profession)) {
                    logger.info("addStudentInfoByExcel::非空字段有空值,已跳过本次循环");
                    logger.warn("第" + (i + 1) + "行数据不正确->非空字段有空值");
                    continue;
                }
                List<Clazz> clazzList = clazzDao.findByClazz(clazz);
                if (clazzList.size() == 0) {
                    logger.info("addStudentInfoByExcel::根据" + clazz + "没有获取到班级,跳过本次循环");
                    logger.warn("第" + (i + 1) + "行数据不正确->根据" + clazz + "没有获取到班级");
                    continue;
                }
                StudentInfo studentInfo = new StudentInfo();
                try {
                    logger.debug("addStudentInfoByExcel::要格式化的日期->" + birthday + "||" + intime);
                    Date birthdayDate = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                    Date inttimeDate = new SimpleDateFormat("yyyy-MM").parse(intime);
                    studentInfo.setBirthday(birthdayDate);
                    studentInfo.setIntime(inttimeDate);
                } catch (ParseException e) {
                    logger.info("addStudentInfoByExcel::日期格式化出错->" + e.getMessage());
                    logger.warn("第" + (i + 1) + "行数据不正确->日期格式化出错->" + e.getMessage());
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
                logger.debug("addStudentInfoByExcel::第" + i + "行学生信息->" + studentInfo);
                studentInfoList.add(studentInfo);
            }
        }
        if (studentInfoList.size() != 0) {
            logger.info("addStudentInfoByExcel::将要添加" + studentInfoList.size() + "条数据");
            studentInfoList.forEach(studentInfoDao::saveAndFlush);
        } else {
            logger.info("addStudentInfoByExcel::集合中数据为0,未添加任何数据");
        }
    }

    private String getCellValue(Row row, int cellNum) {
        String stringCellValue = null;
        Cell cell;
        if ((cell = row.getCell(cellNum)) != null) {
            try {
                stringCellValue = cell.getStringCellValue();
            } catch (IllegalStateException e) {
                logger.info("getCellValue::CellNum->" + cellNum + "<-尝试获取String类型数据失败");
                try {
                    logger.info("getCellValue::CellNum->" + cellNum + "<-尝试获取Double类型数据");
                    stringCellValue = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalStateException e1) {
                    try {
                        logger.info("getCellValue::CellNum->" + cellNum + "<-尝试获取Boolean类型数据");
                        stringCellValue = String.valueOf(cell.getBooleanCellValue());
                    } catch (IllegalStateException e2) {
                        try {
                            logger.info("getCellValue::CellNum->" + cellNum + "<-尝试获取Date类型数据");
                            stringCellValue = String.valueOf(cell.getDateCellValue());
                        } catch (IllegalStateException e3) {
                            logger.warn("getCellValue::CellNum->" + cellNum + "<-未知类型数据->" + e3.getMessage());
                        }
                    }
                }
            } finally {
                logger.debug("getCellValue::第" + cellNum + "格获取到的数据为->" + stringCellValue);
            }
        } else {
            logger.debug("getCellValue::第" + cellNum + "格为空");
        }
        return stringCellValue;
    }
}
