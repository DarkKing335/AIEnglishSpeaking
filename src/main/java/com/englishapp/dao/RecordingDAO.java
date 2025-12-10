package com.englishapp.dao;

import com.englishapp.model.Recording;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RecordingDAO {
    public boolean save(Recording r) {
        String sql = "INSERT INTO recordings (user_id, lesson_id, file_path, created_at) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getLessonId());
            ps.setString(3, r.getFilePath());
            ps.setTimestamp(4, Timestamp.valueOf(r.getCreatedAt()));
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) r.setId(keys.getInt(1)); }
            return true;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public List<Recording> findByUser(int userId) {
        List<Recording> list = new ArrayList<>();
        String sql = "SELECT id, user_id, lesson_id, file_path, created_at FROM recordings WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Recording r = new Recording();
                    r.setId(rs.getInt("id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setLessonId(rs.getInt("lesson_id"));
                    r.setFilePath(rs.getString("file_path"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(r);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public Recording getById(int id) {
        String sql = "SELECT id, user_id, lesson_id, file_path, created_at FROM recordings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Recording r = new Recording();
                    r.setId(rs.getInt("id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setLessonId(rs.getInt("lesson_id"));
                    r.setFilePath(rs.getString("file_path"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return r;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }
}
