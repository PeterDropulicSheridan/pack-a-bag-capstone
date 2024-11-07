window.onload = function() {
  var dropdownButton = document.querySelector('.dropbtn');
  var navLinksContainer = document.querySelector('.dropdown-content');

  function handleResize() {
    if (window.innerWidth <= 768) {
      navLinksContainer.style.display = 'none';
      dropdownButton.style.display = 'block';
      dropdownButton.addEventListener('click', toggleDropdown);
    } else {
      navLinksContainer.style.display = 'flex';
      dropdownButton.style.display = 'none';
      dropdownButton.removeEventListener('click', toggleDropdown);
      resetStyles(); 
    }
  }

  function toggleDropdown() {
    if (navLinksContainer.style.display === 'none' || navLinksContainer.style.display === '') {
        navLinksContainer.style.display = 'flex';
        navLinksContainer.style.flexDirection = 'column';
        navLinksContainer.style.position = 'absolute';
        navLinksContainer.style.left = '0';
        navLinksContainer.style.top = '0';
        navLinksContainer.style.backgroundColor = '#fff'; 
        navLinksContainer.style.zIndex = '999';
        navLinksContainer.style.border = '1px solid #ccc';
    } else {
        navLinksContainer.style.display = 'none';
        resetStyles(); 
    }
  }

  function resetStyles() {
    navLinksContainer.style.position = 'static';
    navLinksContainer.style.backgroundColor = 'transparent';
    navLinksContainer.style.border = 'none';
    navLinksContainer.style.flexDirection = 'row'; 
  }

  window.addEventListener('resize', handleResize);
  handleResize();
}

document.addEventListener("DOMContentLoaded", function() {
  const imageContainers = document.querySelectorAll('.image-container');
  const images = ['https://images.unsplash.com/photo-1460013477427-b0cce3e30151?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D']; // Add paths to your images

  const containerHeight = (265 / 661) * imageContainers[0].offsetWidth;
  imageContainers.forEach(container => {
    container.style.height = containerHeight + 'px';
  });

  imageContainers.forEach((container, index) => {
    const image = container.querySelector('img');
    
    image.src = images[index];
    image.onload = () => {
      image.style.opacity = 1; 

    };
    

  });
});

function redirectToHome() {
    window.location.href = '/home';
}

// LOC review
function checkPasswordStrength() {
    var password = document.getElementById('password').value;
    var strengthBadge = document.getElementById('strength-badge');
    var strength = 0;

    if (password.match(/[a-z]+/)) {
        strength += 1;
    }
    if (password.match(/[A-Z]+/)) {
        strength += 1;
    }
    if (password.match(/[0-9]+/)) {
        strength += 1;
    }
    if (password.length > 8) {
        strength += 1;
    }
    if (password.match(/[!@#$%^&*()_+{}\[\]:;<>,.?~\-]+/)) {
        strength += 1;
    }

    switch (strength) {
        case 1:
            strengthBadge.style.color = "#FF0000"; 
            strengthBadge.textContent = "Password too weak...";
            break;
        case 2:
            strengthBadge.style.color = "#FFD700"; 
            strengthBadge.textContent = "Normal...";
            break;
        case 3:
            strengthBadge.style.color = "#00FF00"; 
            strengthBadge.textContent = "Strong...";
            break;
    }

    var submitButton = document.getElementById('submit-button');
    submitButton.disabled = (strength < 2); 

    if (submitButton.disabled) {
        submitButton.style.animation = "shake 0.5s";
        setTimeout(function() {
            submitButton.style.animation = "none";
        }, 500);
    }
}

function togglePasswordVisibility() {
    var passwordField = document.getElementById("password");
    var passwordCheckbox = document.getElementById("password-toggle");
    passwordField.type = passwordCheckbox.checked ? "text" : "password";
}


let loginAttempts = 0;
const maxAttempts = 5;
let currentCaptcha = '';

function generateCaptcha() {
    const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    let captcha = '';
    for (let i = 0; i < 6; i++) {
        captcha += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return captcha;
}

function drawCaptcha(captcha) {
    const canvas = document.getElementById('captcha-canvas');
    const ctx = canvas.getContext('2d');
    

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    

    ctx.fillStyle = '#f0f0f0';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.font = 'bold 24px Arial';
    ctx.fillStyle = '#333';
    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';

    for (let i = 0; i < 100; i++) {
        ctx.fillStyle = `rgba(0, 0, 0, ${Math.random() * 0.3})`;
        ctx.fillRect(Math.random() * canvas.width, Math.random() * canvas.height, 2, 2);
    }
    

    for (let i = 0; i < captcha.length; i++) {
        ctx.save();
        ctx.translate(20 + i * 30, canvas.height / 2);
        ctx.rotate((Math.random() - 0.5) * 0.3);
        ctx.fillText(captcha[i], 0, 0);
        ctx.restore();
    }
}

function showCaptcha() {
    const captchaContainer = document.getElementById('captcha-container');
    captchaContainer.style.display = 'block';
    currentCaptcha = generateCaptcha();
    drawCaptcha(currentCaptcha);
}

document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form');
    const loginButton = document.getElementById('login-button');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');
    const showPasswordCheckbox = document.getElementById('show-password');
    const captchaContainer = document.getElementById('captcha-container');

   
    loginAttempts = parseInt(localStorage.getItem('loginAttempts') || '0');


    showPasswordCheckbox.addEventListener('change', function() {
        passwordInput.type = this.checked ? 'text' : 'password';
    });

    loginButton.addEventListener('click', function(e) {
        e.preventDefault();

        if (!usernameInput.value || !passwordInput.value) {
            alert('Please fill in all required fields.');
            return;
        }

        if (loginAttempts >= maxAttempts) {
            const captchaInput = document.getElementById('captcha-input');
            
            if (!captchaInput || !captchaInput.value) {
                alert('Please enter the CAPTCHA.');
                return;
            }

            if (captchaInput.value !== currentCaptcha) {
                alert('Invalid CAPTCHA. Please try again.');
                showCaptcha(); 
                return;
            }

            loginAttempts = 0;
            localStorage.setItem('loginAttempts', loginAttempts.toString());
            captchaContainer.style.display = 'none';
        }


        loginAttempts++;
        localStorage.setItem('loginAttempts', loginAttempts.toString());


        if (loginAttempts >= maxAttempts) {
            showCaptcha();
        }

  
        form.submit();
    });


    if (loginAttempts >= maxAttempts) {
        showCaptcha();
    } else {
        captchaContainer.style.display = 'none';
    }
});


window.addEventListener('beforeunload', function() {
    localStorage.setItem('loginAttempts', loginAttempts.toString());
});
