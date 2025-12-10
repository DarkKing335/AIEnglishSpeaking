package com.englishapp.dao;

import com.englishapp.model.Score;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ScoreDAO {
    public boolean save(Score s) {
        String sql = "INSERT INTO scores (user_id, lesson_id, value, created_at) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, s.getUserId());
            ps.setInt(2, s.getLessonId());
            ps.setDouble(3, s.getValue());
            ps.setTimestamp(4, Timestamp.valueOf(s.getCreatedAt()));
            int affected = ps.executeUpdate();
            if (affected == 0) return false;
            try (ResultSet keys = ps.getGeneratedKeys()) { if (keys.next()) s.setId(keys.getInt(1)); }
            return true;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public List<Score> findByUser(int userId) {
        List<Score> list = new ArrayList<>();
        String sql = "SELECT id, user_id, lesson_id, value, created_at FROM scores WHERE user_id = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Score s = new Score();
                    s.setId(rs.getInt("id"));
                    s.setUserId(rs.getInt("user_id"));
                    s.setLessonId(rs.getInt("lesson_id"));
                    s.setValue(rs.getDouble("value"));
                    s.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    list.add(s);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return list;
    }

    public Double findLatestForUserLesson(int userId, int lessonId) {
        String sql = "SELECT value FROM scores WHERE user_id = ? AND lesson_id = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, lessonId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("value");
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }
}
