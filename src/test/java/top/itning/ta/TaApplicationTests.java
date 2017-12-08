package top.itning.ta;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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

}
