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
