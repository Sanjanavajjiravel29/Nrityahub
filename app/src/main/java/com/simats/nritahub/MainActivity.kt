package com.simats.nritahub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.simats.nritahub.ui.academy.JoinAcademyScreen
import com.simats.nritahub.ui.admin.AdministratorDashboardScreen
import com.simats.nritahub.ui.adminprofile.AdministratorProfileScreen
import com.simats.nritahub.ui.attendance.AttendanceHistoryScreen
import com.simats.nritahub.ui.batches.ManageBatchesScreen
import com.simats.nritahub.ui.events.EventsAnnouncementsScreen
import com.simats.nritahub.ui.feemanagement.FeeManagementScreen
import com.simats.nritahub.ui.fees.FeesInvoicesScreen
import com.simats.nritahub.ui.home.HomeScreen
import com.simats.nritahub.ui.home.StudentHomeScreen
import com.simats.nritahub.ui.instructor.InstructorDashboardScreen
import com.simats.nritahub.ui.instructorprofile.InstructorProfileScreen
import com.simats.nritahub.ui.markattendance.MarkAttendanceScreen
import com.simats.nritahub.ui.notifications.NotificationsScreen
import com.simats.nritahub.ui.practice.PracticeLibraryScreen
import com.simats.nritahub.ui.progress.MyProgressScreen
import com.simats.nritahub.ui.review.ReviewPracticeScreen
import com.simats.nritahub.ui.roster.BatchStudentRosterScreen
import com.simats.nritahub.ui.schedule.ClassScheduleScreen
import com.simats.nritahub.ui.splash.SplashScreen
import com.simats.nritahub.ui.submit.SubmitAssignmentScreen
import com.simats.nritahub.ui.theme.NritahubTheme

enum class Screen {
    Splash,                 // Page 1
    JoinAcademy,            // Page 2
    StudentHome,            // Page 7
    ClassSchedule,          // Page 8
    PracticeLibrary,        // Page 10
    SubmitAssignment,       // Page 13
    MyProgress,             // Page 14
    AttendanceHistory,      // Page 15
    FeesInvoices,           // Page 16
    EventsAnnouncements,    // Page 19
    Notifications,          // Page 21
    InstructorDashboard,    // Page 24
    BatchStudentRoster,     // Page 25
    MarkAttendance,         // Page 26
    ReviewPractice,         // Page 27
    InstructorProfile,      // Page 28
    AdministratorDashboard, // Page 29
    ManageBatches,          // Page 30
    FeeManagement,          // Page 31
    AdministratorProfile,   // Page 32
    Home
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NritahubTheme {
                NrityaHubApp()
            }
        }
    }
}

@Composable
fun NrityaHubApp() {
    // Set to Screen.AdministratorDashboard so it launches immediately on device (Page 29)
    var currentScreen by remember { mutableStateOf(Screen.AdministratorDashboard) }

    when (currentScreen) {
        Screen.Splash -> {
            SplashScreen(
                onNavigateToHome = { currentScreen = Screen.JoinAcademy },
                onNavigateToSignIn = { currentScreen = Screen.JoinAcademy }
            )
        }
        Screen.JoinAcademy -> {
            JoinAcademyScreen(
                onBackToSplash = { currentScreen = Screen.Splash },
                onSignInSuccess = { currentScreen = Screen.StudentHome },
                onNavigateToSignUp = { currentScreen = Screen.StudentHome }
            )
        }
        Screen.StudentHome -> {
            StudentHomeScreen(
                onNavigateToClassDetails = { currentScreen = Screen.ClassSchedule },
                onNavigateToSadhana = { currentScreen = Screen.SubmitAssignment },
                onNavigateToEvents = { currentScreen = Screen.EventsAnnouncements },
                onNavigateToNotifications = { currentScreen = Screen.Notifications },
                onNavigateToFees = { currentScreen = Screen.FeesInvoices },
                onNavigateToAttendance = { currentScreen = Screen.AttendanceHistory },
                onNavigateToSchedule = { currentScreen = Screen.ClassSchedule },
                onNavigateToPractice = { currentScreen = Screen.PracticeLibrary },
                onNavigateToProgress = { currentScreen = Screen.MyProgress }
            )
        }
        Screen.ClassSchedule -> {
            ClassScheduleScreen(
                onNavigateToHome = { currentScreen = Screen.StudentHome },
                onNavigateToClassDetail = { currentScreen = Screen.ClassSchedule },
                onNavigateToPractice = { currentScreen = Screen.PracticeLibrary },
                onNavigateToProgress = { currentScreen = Screen.MyProgress }
            )
        }
        Screen.PracticeLibrary -> {
            PracticeLibraryScreen(
                onNavigateToHome = { currentScreen = Screen.StudentHome },
                onNavigateToResourceDetail = { currentScreen = Screen.SubmitAssignment },
                onNavigateToSchedule = { currentScreen = Screen.ClassSchedule },
                onNavigateToProgress = { currentScreen = Screen.MyProgress }
            )
        }
        Screen.SubmitAssignment -> {
            SubmitAssignmentScreen(
                onBackToPrevious = { currentScreen = Screen.PracticeLibrary },
                onSubmitSuccess = { currentScreen = Screen.MyProgress }
            )
        }
        Screen.MyProgress -> {
            MyProgressScreen(
                onNavigateToHome = { currentScreen = Screen.StudentHome },
                onNavigateToSchedule = { currentScreen = Screen.ClassSchedule },
                onNavigateToPractice = { currentScreen = Screen.PracticeLibrary },
                onNavigateToAttendanceHistory = { currentScreen = Screen.AttendanceHistory }
            )
        }
        Screen.AttendanceHistory -> {
            AttendanceHistoryScreen(
                onBackToPrevious = { currentScreen = Screen.MyProgress }
            )
        }
        Screen.FeesInvoices -> {
            FeesInvoicesScreen(
                onBackToPrevious = { currentScreen = Screen.StudentHome }
            )
        }
        Screen.EventsAnnouncements -> {
            EventsAnnouncementsScreen(
                onBackToPrevious = { currentScreen = Screen.StudentHome }
            )
        }
        Screen.Notifications -> {
            NotificationsScreen(
                onBackToPrevious = { currentScreen = Screen.StudentHome }
            )
        }
        Screen.InstructorDashboard -> {
            InstructorDashboardScreen(
                onNavigateToNotifications = { currentScreen = Screen.Notifications },
                onMarkAttendanceClicked = { currentScreen = Screen.MarkAttendance },
                onReviewClicked = { currentScreen = Screen.ReviewPractice },
                onNavigateToBatches = { currentScreen = Screen.BatchStudentRoster },
                onNavigateToProfile = { currentScreen = Screen.InstructorProfile }
            )
        }
        Screen.BatchStudentRoster -> {
            BatchStudentRosterScreen(
                onNavigateToToday = { currentScreen = Screen.InstructorDashboard },
                onNavigateToAttendance = { currentScreen = Screen.MarkAttendance }
            )
        }
        Screen.MarkAttendance -> {
            MarkAttendanceScreen(
                onBackToPrevious = { currentScreen = Screen.InstructorDashboard }
            )
        }
        Screen.ReviewPractice -> {
            ReviewPracticeScreen(
                onBackToPrevious = { currentScreen = Screen.InstructorDashboard }
            )
        }
        Screen.InstructorProfile -> {
            InstructorProfileScreen(
                onNavigateToToday = { currentScreen = Screen.InstructorDashboard },
                onSignOut = { currentScreen = Screen.Splash }
            )
        }
        Screen.AdministratorDashboard -> {
            AdministratorDashboardScreen(
                onNavigateToNotifications = { currentScreen = Screen.Notifications },
                onNavigateToFees = { currentScreen = Screen.FeeManagement },
                onNavigateToSchedule = { currentScreen = Screen.ClassSchedule },
                onNavigateToProfile = { currentScreen = Screen.AdministratorProfile },
                onNavigateToBatches = { currentScreen = Screen.ManageBatches }
            )
        }
        Screen.ManageBatches -> {
            ManageBatchesScreen(
                onNavigateToOverview = { currentScreen = Screen.AdministratorDashboard }
            )
        }
        Screen.FeeManagement -> {
            FeeManagementScreen(
                onNavigateToOverview = { currentScreen = Screen.AdministratorDashboard }
            )
        }
        Screen.AdministratorProfile -> {
            AdministratorProfileScreen(
                onNavigateToOverview = { currentScreen = Screen.AdministratorDashboard },
                onSignOut = { currentScreen = Screen.Splash }
            )
        }
        Screen.Home -> {
            HomeScreen(
                onBackToSplash = { currentScreen = Screen.StudentHome }
            )
        }
    }
}
