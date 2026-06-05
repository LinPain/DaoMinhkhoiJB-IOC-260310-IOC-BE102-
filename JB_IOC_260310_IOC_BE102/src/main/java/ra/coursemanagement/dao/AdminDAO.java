package ra.coursemanagement.dao;

import ra.coursemanagement.model.Admin;

public interface AdminDAO {
    Admin getByUsername(String username);
}
