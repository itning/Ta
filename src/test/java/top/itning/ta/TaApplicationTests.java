package top.itning.ta;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.itning.ta.dao.ClazzDao;
import top.itning.ta.dao.LeaveTypeDao;
import top.itning.ta.dao.StudentInfoDao;
import top.itning.ta.dao.StudentLeaveDao;
import top.itning.ta.entity.Clazz;
import top.itning.ta.entity.LeaveType;
import top.itning.ta.entity.StudentInfo;
import top.itning.ta.entity.StudentLeave;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaApplicationTests {
    @Autowired
    private StudentInfoDao studentInfoDao;

    @Autowired
    private ClazzDao clazzDao;

    @Autowired
    private LeaveTypeDao leaveTypeDao;

    @Autowired
    private StudentLeaveDao studentLeaveDao;


    @Test
    public void contextLoads() {

    }

    @Test
    public void testStudentInfo() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        List<Clazz> clazzList = clazzDao.findAll();
        int i = 0;
        while (true) {
            i++;
            StudentInfo studentInfo = new StudentInfo();
            studentInfo.setImg(uuid);
            studentInfo.setAddress(uuid);
            studentInfo.setBirthday(randomDate("2016-01-01", "2017-12-08"));
            studentInfo.setClazz(clazzList.get(randomInt(0, clazzList.size())));
            studentInfo.setCollege(uuid);
            studentInfo.setHtel(String.valueOf(randomInt(1765223, 1313113131)));
            studentInfo.setTel(String.valueOf(randomInt(1765223, 1313113131)));
            studentInfo.setTeacher(uuid);
            studentInfo.setIntime(randomDate("2016-01-01", "2017-12-08"));
            studentInfo.setPosition(uuid);
            studentInfo.setProfession(uuid);
            studentInfo.setId(String.valueOf(randomInt(1765223, 1313113131)));
            studentInfo.setName(uuid);
            studentInfo.setRemarks(uuid);
            System.out.println(studentInfo);
            studentInfoDao.saveAndFlush(studentInfo);
            if (i == 500) {
                break;
            }
        }
    }

    @Test
    public void testStudentLeave() throws InterruptedException {
        List<LeaveType> leaveTypeList = leaveTypeDao.findAll();
        List<StudentInfo> studentInfoList = studentInfoDao.findAll();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        int i = 0;
        while (true) {
            i++;
            StudentLeave studentLeave = new StudentLeave();
            studentLeave.setId(UUID.randomUUID().toString().replace("-", ""));
            studentLeave.setOften(String.valueOf(randomInt(1, 31)));
            studentLeave.setReason(UUID.randomUUID().toString().replace("-", ""));
            studentLeave.setRemarks(UUID.randomUUID().toString().replace("-", ""));
            studentLeave.setStarttime(randomDate("2016-01-01", "2017-12-08"));
            studentLeave.setMatter(leaveTypeList.get(randomInt(0, leaveTypeList.size())));
            studentLeave.setSid(studentInfoList.get(randomInt(0, studentInfoList.size())));
            System.out.println(studentLeave);
            studentLeaveDao.saveAndFlush(studentLeave);
            if (i == 100) {
                break;
            }
        }
    }

    /**
     * 获取随机日期
     *
     * @param beginDate 起始日期，格式为：yyyy-MM-dd
     * @param endDate   结束日期，格式为：yyyy-MM-dd
     * @return Date
     */
    private static Date randomDate(String beginDate, String endDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);// 构造开始日期
            Date end = format.parse(endDate);// 构造结束日期
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
            if (start.getTime() >= end.getTime()) {
                return null;
            }
            long date = random(start.getTime(), end.getTime());

            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin, long end) {
        long rtn = begin + (long) (Math.random() * (end - begin));
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值
        if (rtn == begin || rtn == end) {
            return random(begin, end);
        }
        return rtn;
    }

    /**
     * @param min 包含
     * @param max 不包含
     * @return
     */
    private int randomInt(int min, int max) {
        return new Random().nextInt(max) % (max - min + 1) + min;
    }

    @Test
    public void testDown() throws IOException {
        Clazz clazz = clazzDao.findOne("c54794e1-e1f6-4b9b-80bf-9f15d12e1119");
        List<StudentInfo> studentInfoList = studentInfoDao.findAllByClazz(clazz);
        studentInfoList.forEach(System.out::println);
        Workbook workbook = new XSSFWorkbook();
        //创建工作簿 名:专业+班级
        Sheet sheet = workbook.createSheet(clazz.getProfession() + clazz.getClazz());

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
        studentInfoList.forEach(studentInfo -> {
            Row dataRow = sheet.createRow(nowCell[0]++);
            Cell IDCell = dataRow.createCell(0);
            IDCell.setCellValue(studentInfo.getId());

            Cell NameCell = dataRow.createCell(1);
            NameCell.setCellValue(studentInfo.getName());

            Cell SexCell = dataRow.createCell(2);
            SexCell.setCellValue(studentInfo.isSex() ? "男" : "女");

            Cell BirthdayCell = dataRow.createCell(3);
            BirthdayCell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(studentInfo.getBirthday()));

            Cell TelCell = dataRow.createCell(4);
            TelCell.setCellValue(studentInfo.getTel());

            Cell HtelCell = dataRow.createCell(5);
            HtelCell.setCellValue(studentInfo.getHtel());

            Cell IntimeCell = dataRow.createCell(6);
            IntimeCell.setCellValue(new SimpleDateFormat("yyyy-MM").format(studentInfo.getIntime()));

            Cell InCell = dataRow.createCell(7);
            InCell.setCellValue(studentInfo.isIsin() ? "是" : "否");

            Cell PositionCell = dataRow.createCell(8);
            PositionCell.setCellValue(studentInfo.getPosition());

            Cell TeacherCell = dataRow.createCell(9);
            TeacherCell.setCellValue(studentInfo.getTeacher());

            Cell ClassCell = dataRow.createCell(10);
            ClassCell.setCellValue(studentInfo.getClazz().getClazz());

            Cell AddressCell = dataRow.createCell(11);
            AddressCell.setCellValue(studentInfo.getAddress());

            Cell CollegeCell = dataRow.createCell(12);
            CollegeCell.setCellValue(studentInfo.getCollege());

            Cell ProfessionCell = dataRow.createCell(13);
            ProfessionCell.setCellValue(studentInfo.getProfession());

            Cell RemarksCell = dataRow.createCell(13);
            RemarksCell.setCellValue(studentInfo.getRemarks());
        });
        FileOutputStream fileOutputStream = new FileOutputStream(new File("C:/Users/Ning/Desktop/test.xlsx"));
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        workbook.close();
    }

    @Test
    public void testString() {
        String str = "1017322085-1020145467-1037459874-1045385098-1079119260-1080062371-1086758172";
        String[] split = StringUtils.split(str, "-");
        System.out.println(split.length);
        System.out.println(Arrays.toString(split));
    }
}
