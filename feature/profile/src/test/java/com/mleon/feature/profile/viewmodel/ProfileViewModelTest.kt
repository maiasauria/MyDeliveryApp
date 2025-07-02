package com.mleon.feature.profile.viewmodel

import org.junit.Assert.*
 class ProfileViewModelTest

// GIVEN userRepository returns user profile
// WHEN ProfileViewModel is initialized
// THEN uiState contains user profile data

// GIVEN userRepository throws exception
// WHEN ProfileViewModel is initialized
// THEN uiState contains error message

// GIVEN valid profile data
// WHEN onUpdateProfile is called and update succeeds
// THEN uiState shows success message and updated profile

// GIVEN valid profile data
// WHEN onUpdateProfile is called and update fails
// THEN uiState shows error message

// GIVEN user is logged in
// WHEN onLogout is called
// THEN uiState resets to initial state and triggers logout event

// GIVEN profile is loading
// WHEN loading completes
// THEN uiState sets isLoading to false