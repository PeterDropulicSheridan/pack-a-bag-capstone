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
  const images = ['https://images.unsplash.com/photo-1622560480605-d83c853bc5c3?q=80&w=1976&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D']; 

  const containerHeight = (265 / 661) * imageContainers[0].offsetWidth;
  imageContainers.forEach(container => {
    container.style.height = containerHeight + 'px';
  });

  imageContainers.forEach((container, index) => {
    const image = container.querySelector('img');
    
    
    image.src = images[index];
    image.onload = () => {
      image.style.opacity = 1; 
      image.style.animation = 'moveImage 5s linear infinite'; 
    };
    
    setInterval(() => {
      image.style.opacity = 0; 
      setTimeout(() => {
        image.src = images[index]; 
        image.onload = () => {
          image.style.opacity = 1; 
          image.style.animation = 'moveImage 5s linear infinite'; 
        };
      }, 500); 
      index = (index + 1) % images.length; 
    }, 5000); 
  });
});

 function redirectToHome() {
        window.location.href = '/home';
    }
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
        if (password.length > 7) {
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
