<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- Define menu items
--%>
<c:set
  var="menuItems"
  value="${[
    {'label': 'Home', 'url': '/'},
    {'label': 'Application', 'url': '/job_application'},
    {'label': 'Posting', 'url': '/job_posting'},
    {'label': 'Employee', 'url': '/employee'},
    {'label': 'Request', 'url': '/request'},
    {'label': 'Task', 'url': '/task'},
    {'label': 'Contact', 'url': '/contact'}
]}"
  scope="page"
/>
<!-- Google Fonts - Inter -->
<link rel="preconnect" href="https://fonts.googleapis.com" />
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
<link
  href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap"
  rel="stylesheet"
/>
<link
  rel="stylesheet"
  href="${pageContext.request.contextPath}/css/header.css"
/>
<link
  rel="stylesheet"
  href="${pageContext.request.contextPath}/css/theme-toggle.css"
/>
<link
  rel="stylesheet"
  href="${pageContext.request.contextPath}/css/mobile-menu.css"
/>

<!-- Load SVG Icons Sprite -->
<jsp:include page="/images/icons.svg" />

<!-- Mobile menu overlay -->
<div class="mobile-overlay" id="mobileOverlay"></div>

<header class="header">
  <div class="container">
    <div class="header__logo">iHRM</div>

    <button class="header__hamburger" id="hamburger" aria-label="Menu">
      <span></span>
      <span></span>
      <span></span>
    </button>

    <nav class="header__nav" id="nav">
      <c:forEach var="item" items="${menuItems}">
        <a href="${pageContext.request.contextPath}${item.url}"
          >${item.label}</a
        >
      </c:forEach>
      <div class="header__mobile-actions">
        <button
          class="header__theme-toggle header__theme-toggle--mobile"
          id="themeToggleMobile"
          aria-label="Toggle theme"
        >
          <svg class="theme-icon theme-icon--light" width="20" height="20">
            <use href="#sun"></use>
          </svg>
          <svg class="theme-icon theme-icon--dark" width="20" height="20">
            <use href="#moon"></use>
          </svg>
        </button>
        <button
          class="header__login-btn header__login-btn--mobile"
          onclick="window.location.href='${pageContext.request.contextPath}/login'"
        >
          Login
        </button>
      </div>
    </nav>

    <div class="header__actions">
      <button
        class="header__theme-toggle"
        id="themeToggle"
        aria-label="Toggle theme"
      >
        <svg class="theme-icon theme-icon--light" width="20" height="20">
          <use href="#sun"></use>
        </svg>
        <svg class="theme-icon theme-icon--dark" width="20" height="20">
          <use href="#moon"></use>
        </svg>
      </button>
      <button
        class="header__login-btn header__login-btn--desktop"
        onclick="window.location.href='${pageContext.request.contextPath}/login'"
      >
        Login
      </button>
    </div>
  </div>
</header>

<script>
  const hamburger = document.getElementById("hamburger");
  const nav = document.getElementById("nav");
  const mobileOverlay = document.getElementById("mobileOverlay");
  const themeToggle = document.getElementById("themeToggle");

  hamburger.addEventListener("click", function () {
    this.classList.toggle("active");
    nav.classList.toggle("active");
    mobileOverlay.classList.toggle("active");
    document.body.style.overflow = nav.classList.contains("active")
      ? "hidden"
      : "";
  });

  // Close menu when clicking overlay
  mobileOverlay.addEventListener("click", function () {
    hamburger.classList.remove("active");
    nav.classList.remove("active");
    mobileOverlay.classList.remove("active");
    document.body.style.overflow = "";
  });

  // Close menu when clicking outside
  document.addEventListener("click", function (event) {
    const isClickInsideNav = nav.contains(event.target);
    const isClickOnHamburger = hamburger.contains(event.target);

    if (
      !isClickInsideNav &&
      !isClickOnHamburger &&
      nav.classList.contains("active")
    ) {
      hamburger.classList.remove("active");
      nav.classList.remove("active");
      mobileOverlay.classList.remove("active");
      document.body.style.overflow = "";
    }
  });

  // Theme toggle functionality
  const savedTheme = localStorage.getItem("theme") || "light";
  document.documentElement.setAttribute("data-theme", savedTheme);

  const themeToggleMobile = document.getElementById("themeToggleMobile");

  function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute("data-theme");
    const newTheme = currentTheme === "light" ? "dark" : "light";

    document.documentElement.setAttribute("data-theme", newTheme);
    localStorage.setItem("theme", newTheme);
  }

  themeToggle.addEventListener("click", toggleTheme);
  themeToggleMobile.addEventListener("click", toggleTheme);
</script>
