// Authentication header function
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
    };
}

// Check if user is logged in
function checkAuth() {
    if (!localStorage.getItem('token')) {
        window.location.href = 'login.html';
    }
}

// Fetch user profile data
async function fetchUserProfile() {
    try {
        const response = await fetch('/api/profile', {
            method: 'GET',
            headers: getAuthHeaders()
        });
        
        if (!response.ok) {
            throw new Error('Failed to fetch profile');
        }
        
        const userData = await response.json();
        populateProfile(userData);
    } catch (error) {
        console.error('Error fetching profile:', error);
        document.getElementById('error-alert').textContent = 'Failed to load profile data.';
        document.getElementById('error-alert').classList.remove('d-none');
    }
}

function populateProfile(userData) {
    // Check if data is nested inside a data property
    const profileData = userData.data || userData;
    
    // Update form fields
    document.getElementById('name').value = profileData.name || '';
    document.getElementById('email').value = profileData.email || '';
    document.getElementById('role').value = profileData.role || '';
    
    // Handle doctor-specific fields if present
    if (document.getElementById('specialty')) {
        document.getElementById('specialty').value = profileData.specialty || '';
    }
    if (document.getElementById('workingAddress')) {
        document.getElementById('workingAddress').value = profileData.workingAddress || '';
    }
    if (document.getElementById('address')) {
        document.getElementById('address').value = profileData.address || '';
    }
    if (document.getElementById('phonenum')) {
        document.getElementById('phonenum').value = profileData.phonenum || '';
    }
    if (document.getElementById('phoneNumber')) {
        document.getElementById('phoneNumber').value = profileData.phonenum || '';
    }

    // Update header info
    document.getElementById('user-name').textContent = profileData.name || '';
    document.getElementById('user-email').textContent = profileData.email || '';
    document.getElementById('user-role').textContent = profileData.role || '';
    
    // Set avatar initial
    if (profileData.name) {
        document.getElementById('user-initial').textContent = profileData.name.charAt(0).toUpperCase();
    }
    
    // Show/hide doctor-specific sections based on role
    const doctorInfoSection = document.querySelector('.doctor-info');
    const patientInfoSection = document.querySelector('.patient-info');
    const ratingsSection = document.querySelector('.card.mt-4');
    
    if (doctorInfoSection && ratingsSection) {
        if (profileData.role === 'DOCTOR') {
            doctorInfoSection.style.display = 'block';
            ratingsSection.style.display = 'block';
            patientInfoSection.style.display = 'none';

        } else {
            doctorInfoSection.style.display = 'none';
            ratingsSection.style.display = 'none';
            patientInfoSection.style.display = 'block';
        }
    }
    
    // Store user ID for other functions
    window.currentUserId = profileData.id;
}

// Update profile function
async function updateProfile(event) {
    event.preventDefault();
    
    const profileData = {
        name: document.getElementById('name').value
    };
    
    const currentRole = document.getElementById('role').value;
    
    if (currentRole === 'DOCTOR')  {
        profileData.specialty = document.getElementById('specialty').value;
        profileData.workingAddress = document.getElementById('workingAddress').value;
        profileData.phonenum = document.getElementById('phonenum').value;
    } else if (currentRole === 'PATIENT') {
        profileData.nik = document.getElementById('nik').value;
        profileData.address = document.getElementById('address').value;
        profileData.phonenum = document.getElementById('phoneNumber').value;
    }
    
    
    try {
        const response = await fetch('/api/profile', {
            method: 'PUT',
            headers: getAuthHeaders(),
            body: JSON.stringify(profileData)
        });
        
        if (!response.ok) {
            throw new Error('Failed to update profile');
        }
        
        document.getElementById('success-alert').classList.remove('d-none');
        setTimeout(() => {
            document.getElementById('success-alert').classList.add('d-none');
        }, 3000);
        
        fetchUserProfile();
    } catch (error) {
        console.error('Error updating profile:', error);
        document.getElementById('error-alert').textContent = 'Failed to update profile.';
        document.getElementById('error-alert').classList.remove('d-none');
    }
}

// Get doctor ID for the current profile
async function getDoctorId() {
    // Use stored ID if available
    if (window.currentUserId) {
        return window.currentUserId;
    }
    
    try {
        const response = await fetch('/api/profile', {
            headers: getAuthHeaders()
        });
        const userData = await response.json();
        return (userData.data || userData).id;
    } catch (error) {
        console.error('Error getting doctor ID:', error);
        return null;
    }
}


// Logout function
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    window.location.href = 'login.html';
}








// Load ratings for the doctor
async function loadDoctorRatings() {
    const doctorId = await getDoctorId();
    if (!doctorId) return;
    
    // Only load ratings if viewing a doctor profile
    if (document.getElementById('role').value !== 'DOCTOR') {
        document.querySelector('.card.mt-4').style.display = 'none';
        return;
    }
    
    fetch(`/api/v1/ratings/doctor/${doctorId}`, {
        headers: getAuthHeaders()
    })
    .then(response => response.json())
    .then(ratings => {
        // Load average rating
        fetch(`/api/v1/ratings/doctor/${doctorId}/average`, {
            headers: getAuthHeaders()
        })
        .then(response => response.json())
        .then(avgRating => {
            document.getElementById('avgRating').textContent = avgRating.toFixed(1);
            updateStarDisplay(avgRating);
        });
        
        // Display all ratings
        displayRatings(ratings);
    })
    .catch(error => {
        console.error('Error loading ratings:', error);
        document.getElementById('loadingRatings').textContent = 'Error loading ratings.';
    });
}

// Display ratings in the ratings list
function displayRatings(ratings) {
    const ratingsList = document.getElementById('ratingsList');
    ratingsList.innerHTML = '';
    
    if (ratings.length === 0) {
        ratingsList.innerHTML = '<p class="text-center">No ratings yet.</p>';
        return;
    }
    
    ratings.forEach(rating => {
        const ratingItem = document.createElement('div');
        ratingItem.className = 'rating-item border-bottom pb-3 mb-3';
        
        ratingItem.innerHTML = `
            <div class="d-flex justify-content-between">
                <strong>${rating.patientName}</strong>
                <span class="text-warning">${rating.score}/5 ★</span>
            </div>
            <p class="mt-2">${rating.review}</p>
        `;
        
        ratingsList.appendChild(ratingItem);
    });
}

// Update star display based on rating
function updateStarDisplay(rating) {
    const avgStars = document.getElementById('avgStars');
    const fullStars = Math.round(rating);
    
    let starsHtml = '';
    for (let i = 1; i <= 5; i++) {
        if (i <= fullStars) {
            starsHtml += '★';
        } else {
            starsHtml += '☆';
        }
    }
    
    avgStars.textContent = starsHtml;
}

// Event listeners
document.addEventListener('DOMContentLoaded', function() {
    checkAuth();
    fetchUserProfile();
    loadDoctorRatings();
    
    document.getElementById('profile-form').addEventListener('submit', updateProfile);
    document.getElementById('logout-btn').addEventListener('click', logout);
    
}); 