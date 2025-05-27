// Navbar loader utility
async function loadNavbar() {
  try {
    const response = await fetch('/navbar.html');
    if (!response.ok) {
      throw new Error('Failed to load navbar');
    }
    const navbarHTML = await response.text();
    
    // Create a temporary container to parse the HTML
    const tempDiv = document.createElement('div');
    tempDiv.innerHTML = navbarHTML;
    
    // Extract and inject the navbar content
    const navElement = tempDiv.querySelector('nav');
    const styleElement = tempDiv.querySelector('style');
    const scriptElement = tempDiv.querySelector('script');
    
    if (navElement) {
      document.body.insertBefore(navElement, document.body.firstChild);
    }
    
    if (styleElement) {
      // Add styles to head
      document.head.appendChild(styleElement);
    }
    
    if (scriptElement) {
      // Execute script content
      const newScript = document.createElement('script');
      newScript.textContent = scriptElement.textContent;
      document.head.appendChild(newScript);
    }
  } catch (error) {
    console.error('Error loading navbar:', error);
  }
}

// Load navbar when DOM is ready
document.addEventListener('DOMContentLoaded', loadNavbar);