package org.example.gymmanagement;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import util.DBConnection;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ExerciseController {

    // ---------------- Timers and reps ----------------
    @FXML private Label timer1, timer2, timer3, timer4, timer5, timer6, timer7, timer8, timer9, timer10, timer11, timer12;
    @FXML private Label reps1, reps2, reps3, reps4, reps5, reps6, reps7, reps8, reps9, reps10, reps11, reps12;
    @FXML private Button attendanceBtn;

    private int[] seconds = new int[12];    // Seconds for each exercise
    private int[] reps = new int[12];       // Reps for each exercise
    private Timeline[] timelines = new Timeline[12];

    private int memberId = 1; // TODO: Set this after login

    // ---------------- Initialize ----------------
    @FXML
    private void initialize() {
        // Create Timelines for all exercises
        for (int i = 0; i < 12; i++) {
            final int index = i;
            timelines[i] = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer(index)));
            timelines[i].setCycleCount(Timeline.INDEFINITE);
        }
    }

    // ---------------- Timer Methods ----------------
    private void updateTimer(int index) {
        seconds[index]++;
        getTimerLabel(index).setText(formatTime(seconds[index]));
    }

    private Label getTimerLabel(int index) {
        switch(index) {
            case 0: return timer1;
            case 1: return timer2;
            case 2: return timer3;
            case 3: return timer4;
            case 4: return timer5;
            case 5: return timer6;
            case 6: return timer7;
            case 7: return timer8;
            case 8: return timer9;
            case 9: return timer10;
            case 10: return timer11;
            case 11: return timer12;
            default: return null;
        }
    }

    private Label getRepsLabel(int index) {
        switch(index) {
            case 0: return reps1;
            case 1: return reps2;
            case 2: return reps3;
            case 3: return reps4;
            case 4: return reps5;
            case 5: return reps6;
            case 6: return reps7;
            case 7: return reps8;
            case 8: return reps9;
            case 9: return reps10;
            case 10: return reps11;
            case 11: return reps12;
            default: return null;
        }
    }

    private String formatTime(int totalSeconds) {
        int min = totalSeconds / 60;
        int sec = totalSeconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    // ---------------- Start/Pause/Stop methods ----------------
    @FXML private void startTimer1() { timelines[0].play(); }
    @FXML private void pauseTimer1() { timelines[0].pause(); }
    @FXML private void stopTimer1() { timelines[0].stop(); saveWorkout(0); seconds[0] = 0; getTimerLabel(0).setText("00:00"); }

    @FXML private void startTimer2() { timelines[1].play(); }
    @FXML private void pauseTimer2() { timelines[1].pause(); }
    @FXML private void stopTimer2() { timelines[1].stop(); saveWorkout(1); seconds[1] = 0; getTimerLabel(1).setText("00:00"); }

    @FXML private void startTimer3() { timelines[2].play(); }
    @FXML private void pauseTimer3() { timelines[2].pause(); }
    @FXML private void stopTimer3() { timelines[2].stop(); saveWorkout(2); seconds[2] = 0; getTimerLabel(2).setText("00:00"); }

    @FXML private void startTimer4() { timelines[3].play(); }
    @FXML private void pauseTimer4() { timelines[3].pause(); }
    @FXML private void stopTimer4() { timelines[3].stop(); saveWorkout(3); seconds[3] = 0; getTimerLabel(3).setText("00:00"); }

    @FXML private void startTimer5() { timelines[4].play(); }
    @FXML private void pauseTimer5() { timelines[4].pause(); }
    @FXML private void stopTimer5() { timelines[4].stop(); saveWorkout(4); seconds[4] = 0; getTimerLabel(4).setText("00:00"); }

    @FXML private void startTimer6() { timelines[5].play(); }
    @FXML private void pauseTimer6() { timelines[5].pause(); }
    @FXML private void stopTimer6() { timelines[5].stop(); saveWorkout(5); seconds[5] = 0; getTimerLabel(5).setText("00:00"); }

    @FXML private void startTimer7() { timelines[6].play(); }
    @FXML private void pauseTimer7() { timelines[6].pause(); }
    @FXML private void stopTimer7() { timelines[6].stop(); saveWorkout(6); seconds[6] = 0; getTimerLabel(6).setText("00:00"); }

    @FXML private void startTimer8() { timelines[7].play(); }
    @FXML private void pauseTimer8() { timelines[7].pause(); }
    @FXML private void stopTimer8() { timelines[7].stop(); saveWorkout(7); seconds[7] = 0; getTimerLabel(7).setText("00:00"); }

    @FXML private void startTimer9() { timelines[8].play(); }
    @FXML private void pauseTimer9() { timelines[8].pause(); }
    @FXML private void stopTimer9() { timelines[8].stop(); saveWorkout(8); seconds[8] = 0; getTimerLabel(8).setText("00:00"); }

    @FXML private void startTimer10() { timelines[9].play(); }
    @FXML private void pauseTimer10() { timelines[9].pause(); }
    @FXML private void stopTimer10() { timelines[9].stop(); saveWorkout(9); seconds[9] = 0; getTimerLabel(9).setText("00:00"); }

    @FXML private void startTimer11() { timelines[10].play(); }
    @FXML private void pauseTimer11() { timelines[10].pause(); }
    @FXML private void stopTimer11() { timelines[10].stop(); saveWorkout(10); seconds[10] = 0; getTimerLabel(10).setText("00:00"); }

    @FXML private void startTimer12() { timelines[11].play(); }
    @FXML private void pauseTimer12() { timelines[11].pause(); }
    @FXML private void stopTimer12() { timelines[11].stop(); saveWorkout(11); seconds[11] = 0; getTimerLabel(11).setText("00:00"); }

    // ---------------- +Rep Methods ----------------
    @FXML private void addRep1() { addRep(0); }
    @FXML private void addRep2() { addRep(1); }
    @FXML private void addRep3() { addRep(2); }
    @FXML private void addRep4() { addRep(3); }
    @FXML private void addRep5() { addRep(4); }
    @FXML private void addRep6() { addRep(5); }
    @FXML private void addRep7() { addRep(6); }
    @FXML private void addRep8() { addRep(7); }
    @FXML private void addRep9() { addRep(8); }
    @FXML private void addRep10() { addRep(9); }
    @FXML private void addRep11() { addRep(10); }
    @FXML private void addRep12() { addRep(11); }

    private void addRep(int index) {
        reps[index]++;
        getRepsLabel(index).setText(reps[index] + " reps");
    }

    // ---------------- Save Workout to SQLite ----------------

    private void saveWorkout(int index) {
        String[] exerciseNames = {
                "Push Ups","Squats","Sit Ups","Lunges","Plank","Burpees",
                "Jumping Jacks","Mountain Climbers","Leg Raises","Bicep Curls","Tricep Dips","Crunches"
        };

        String exercise = exerciseNames[index];
        int performedReps = reps[index];

        String query = "INSERT INTO workout(member_id, exercise, reps, day) VALUES(?,?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, memberId);
            pst.setString(2, exercise);
            pst.setInt(3, performedReps);
            pst.setString(4, LocalDate.now().toString());

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✅ SAVED: " + exercise + " - " + performedReps + " reps");
            }

        } catch (SQLException e) {
            System.err.println("❌ Save failed: " + e.getMessage());
            e.printStackTrace();
        }

        // Reset
        reps[index] = 0;
        getRepsLabel(index).setText("0 reps");
    }


    // ---------------- Attendance ----------------
    @FXML
    private void markAttendance() {
        String query = "INSERT INTO attendance(member_id, date, status) VALUES(?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            pst.setString(2, LocalDate.now().toString());
            pst.setString(3, "Present");
            pst.executeUpdate();
            System.out.println("Attendance recorded for " + LocalDate.now());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    // Inside ExerciseController class
    @FXML





    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void goToUserPanel(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userPanel.fxml"));
            Scene scene = new Scene(loader.load());

            // ✅ CRITICAL: Get USER_ID from member_id first
            userController controller = loader.getController();
            int userId = getUserIdFromMemberId(); // Add this method
            controller.setUserId(userId);  // Pass USER_ID, not memberId

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("User Panel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getUserIdFromMemberId() {
        String query = "SELECT user_id FROM members WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}

