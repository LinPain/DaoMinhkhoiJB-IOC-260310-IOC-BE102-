package ra.coursemanagement.business.impl;

import org.mindrot.jbcrypt.BCrypt;
import ra.coursemanagement.business.AdminService;
import ra.coursemanagement.dao.AdminDAO;
import ra.coursemanagement.dao.impl.AdminDAOImpl;
import ra.coursemanagement.model.Admin;

public class AdminServiceImpl implements AdminService {
    private final AdminDAO adminDAO = new AdminDAOImpl();

    @Override
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return false;
        }
        Admin admin = adminDAO.getByUsername(username);
        if (admin != null) {
            // Khớp mật khẩu sử dụng thư viện băm jBCrypt
            return BCrypt.checkpw(password, admin.getPassword());
        }
        return false;
    }
}
