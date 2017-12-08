package top.itning.ta.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.itning.ta.entity.LeaveType;

/**
 * LEAVE_TYPE Table Dao
 *
 * @author wangn
 */
public interface LeaveTypeDao extends JpaRepository<LeaveType, String> {
}
