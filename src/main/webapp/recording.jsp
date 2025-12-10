<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.englishapp.dao.RecordingDAO, com.englishapp.dao.LessonDAO, com.englishapp.dao.ScoreDAO, com.englishapp.model.Recording, com.englishapp.model.Lesson, com.englishapp.model.User, com.englishapp.service.AIService" %>
<%
  String idParam = request.getParameter("id");
  RecordingDAO recordingDAO = new RecordingDAO();
  Recording r = null;
  if (idParam != null) {
    try {
      r = recordingDAO.getById(Integer.parseInt(idParam));
    } catch (NumberFormatException e) {
      // ignore
    }
  }

  Lesson lesson = null;
  Double score = null;
  String feedback = null;
  if (r != null) {
    LessonDAO lessonDAO = new LessonDAO();
    lesson = lessonDAO.getById(r.getLessonId());
    ScoreDAO scoreDAO = new ScoreDAO();
    score = scoreDAO.findLatestForUserLesson(r.getUserId(), r.getLessonId());
    AIService ai = new AIService();
    feedback = ai.generateFeedback(null); // placeholder
  }
%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <title>Recording Details</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="font-sans bg-gray-100 p-8">
    <div class="max-w-3xl mx-auto bg-white rounded-xl shadow p-6">
      <% if (r == null) { %>
      <h2 class="text-xl font-bold">Recording not found</h2>
      <p class="mt-4">The requested recording could not be found.</p>
      <% } else { %>
      <h2 class="text-2xl font-bold">
        <%= (lesson != null ? lesson.getTitle() : "Recording") %>
      </h2>
      <p class="text-sm text-gray-500">
        Recorded: <%= r.getCreatedAt().toString() %>
      </p>

      <div class="mt-6">
        <audio controls preload="metadata" class="w-full">
          <source src="<%= r.getFilePath() %>" />
          Your browser does not support the audio element.
        </audio>
      </div>

      <div class="mt-6 grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="p-4 border rounded">
          <p class="text-sm text-gray-600">Overall Score</p>
          <p class="text-3xl font-bold text-primary">
            <%= (score != null ? String.format("%d", Math.round(score)) : "-")
            %>/100
          </p>
        </div>
        <div class="p-4 border rounded md:col-span-2">
          <p class="text-sm text-gray-600 font-bold">AI Feedback</p>
          <p class="mt-2 text-gray-700">
            <%= (feedback != null ? feedback : "No feedback available.") %>
          </p>
        </div>
      </div>

        <div class="mt-6">
        <h3 class="font-bold">Full Breakdown</h3>
        <p class="text-sm text-gray-600">
          Detailed breakdown is not yet stored. Placeholder components:
        </p>
        <div class="mt-3 space-y-3">
          <div>
            <div class="flex justify-between text-sm text-gray-700">
              <span>Accuracy</span
              ><span
                  ><%= (score != null ? String.format("%d%%", Math.round(Math.min(100, score))) : "-") %></span>
            </div>
            <div class="w-full bg-gray-200 rounded h-2 mt-1">
                <div class="bg-primary h-2 rounded" style="width: <%= (score != null ? Math.min(100, score) : 0) %>%"></div>
            </div>
          </div>
          <div>
            <div class="flex justify-between text-sm text-gray-700">
              <span>Fluency</span
              ><span
                  ><%= (score != null ? String.format("%d%%", Math.round(Math.max(0, Math.min(100, score * 0.9)))) : "-") %></span>
            </div>
            <div class="w-full bg-gray-200 rounded h-2 mt-1">
                <div class="bg-primary h-2 rounded" style="width: <%= (score != null ? Math.max(0, Math.min(100, score * 0.9)) : 0) %>%"></div>
            </div>
          </div>
          <div>
            <div class="flex justify-between text-sm text-gray-700">
              <span>Pronunciation</span
              ><span
                  ><%= (score != null ? String.format("%d%%", Math.round(Math.max(0, Math.min(100, score * 0.95)))) : "-") %></span>
            </div>
            <div class="w-full bg-gray-200 rounded h-2 mt-1">
                <div class="bg-primary h-2 rounded" style="width: <%= (score != null ? Math.max(0, Math.min(100, score * 0.95)) : 0) %>%"></div>
            </div>
          </div>
        </div>
      </div>
      <% } %>
    </div>
  </body>
</html>
