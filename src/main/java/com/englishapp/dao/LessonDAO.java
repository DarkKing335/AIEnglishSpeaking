package com.englishapp.dao;

import com.englishapp.model.Lesson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO {
    public List<Lesson> listAll() {
        List<Lesson> lessons = new ArrayList<>();
        String sql = "SELECT id, title, content FROM lessons ORDER BY id";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lessons.add(new Lesson(rs.getInt("id"), rs.getString("title"), rs.getString("content")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lessons;
    }

    public Lesson getById(int id) {
        String sql = "SELECT id, title, content FROM lessons WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Lesson(rs.getInt("id"), rs.getString("title"), rs.getString("content"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
