package com.edusys.dao;

import com.edusys.entity.NhanVien;
import com.edusys.utils.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp này thực hiện các thao tác với bảng NhanVien trong cơ sở dữ liệu.
 */
public class NhanVienDAO extends EduSysDAO<NhanVien, String> {

    // Thêm nhân viên vào cơ sở dữ liệu
    public void insert(NhanVien model) {
        String sql = "INSERT INTO NhanVien (MaNV, MatKhau, HoTen, VaiTro) VALUES (?, ?, ?, ?)";
        XJdbc.update(sql, 
                model.getMaNV(), 
                model.getMatKhau(), 
                model.getHoTen(), 
                model.isVaiTro());
    }

    // Cập nhật thông tin nhân viên
    public void update(NhanVien model) {
        String sql = "UPDATE NhanVien SET MatKhau=?, HoTen=?, VaiTro=? WHERE MaNV=?";
        XJdbc.update(sql, 
                model.getMatKhau(), 
                model.getHoTen(), 
                model.isVaiTro(),
                model.getMaNV());
    }

    // Xóa nhân viên khỏi cơ sở dữ liệu
    public void delete(String MaNV) {
        String sql = "DELETE FROM NhanVien WHERE MaNV=?";
        XJdbc.update(sql, MaNV);
    }

    // Lấy tất cả nhân viên từ cơ sở dữ liệu
    public List<NhanVien> selectAll() {
        String sql = "SELECT * FROM NhanVien";
        return this.selectBySql(sql);
    }

    // Lấy nhân viên theo mã nhân viên
    public NhanVien selectById(String manv) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV=?";
        List<NhanVien> list = this.selectBySql(sql, manv);
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy nhân viên với MaNV: " + manv);
            return null;
        }
        return list.get(0);
    }

    // Phương thức giúp thực hiện truy vấn SQL và trả về danh sách nhân viên
    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        ResultSet rs = null;
        try {
            // Gọi phương thức XJdbc.query để lấy ResultSet
            rs = XJdbc.query(sql, args);

            // Kiểm tra nếu rs không phải null trước khi xử lý
            if (rs != null) {
                while (rs.next()) {
                    NhanVien entity = new NhanVien();
                    entity.setMaNV(rs.getString("MaNV"));
                    entity.setMatKhau(rs.getString("MatKhau"));
                    entity.setHoTen(rs.getString("HoTen"));
                    entity.setVaiTro(rs.getBoolean("VaiTro"));
                    list.add(entity);
                }
            } else {
                System.out.println("Không có dữ liệu trả về từ truy vấn SQL.");
            }
        } catch (SQLException ex) {
            System.out.println("Lỗi SQL khi truy vấn: " + sql);
            ex.printStackTrace();
            throw new RuntimeException(ex);  // Nếu có lỗi SQL, in ra lỗi và ném ngoại lệ
        } finally {
            try {
                // Đảm bảo kết nối được đóng nếu rs không phải null
                if (rs != null) {
                    rs.getStatement().getConnection().close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }
}
