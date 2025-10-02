<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>iHRM - Smart HR Management System</title>
    <!-- Google Fonts - Inter -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap"
      rel="stylesheet"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/global.css"
    />
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/index.css"
    />
  </head>
  <body>
    <%@ include file="common/header.jsp" %>

    <section class="hero">
      <div class="container">
        <div class="row align-center">
          <div class="col-12 col-md-6">
            <div class="hero__content">
              <h1 class="hero__title"><span class="typing-text"></span></h1>
              <p class="hero__subtitle">
                Automate attendance, payroll, and performance tracking in one
                platform. Streamline your HR processes with intelligent
                automation.
              </p>
              <div class="hero__actions">
                <button
                  class="btn btn-primary btn-lg"
                  onclick="window.location.href='${pageContext.request.contextPath}/login'"
                >
                  Start now
                </button>
                <button class="btn btn-outline btn-lg">Learn more</button>
              </div>
            </div>
          </div>
          <div class="col-12 col-md-6">
            <div class="hero__media">
              <!-- <img
                src="${pageContext.request.contextPath}/images/HomePage/image-removebg-preview.png"
                alt="iHRM Illustration"
                class="hero__image"
              /> -->
              <!-- YouTube Video -->
              <iframe
                class="hero__video"
                src="https://www.youtube.com/embed/Llw9Q6akRo4?si=fNQCQZ8JLD83g0Lg&amp;controls=0&autoplay=1"
                title="YouTube video player"
                frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                allowfullscreen
              >
              </iframe>
            </div>
          </div>
        </div>
      </div>
    </section>

    <script>
      // Typing effect loop
      const texts = ["Smart HR Management System"];
      let textIndex = 0;
      let charIndex = 0;
      let isDeleting = false;
      const typingSpeed = 100;
      const deletingSpeed = 50;
      const pauseTime = 2000;

      function typeEffect() {
        const typingElement = document.querySelector(".typing-text");
        const currentText = texts[textIndex];

        if (!isDeleting && charIndex < currentText.length) {
          typingElement.textContent += currentText.charAt(charIndex);
          charIndex++;
          setTimeout(typeEffect, typingSpeed);
        } else if (isDeleting && charIndex > 0) {
          typingElement.textContent = currentText.substring(0, charIndex - 1);
          charIndex--;
          setTimeout(typeEffect, deletingSpeed);
        } else if (!isDeleting && charIndex === currentText.length) {
          isDeleting = true;
          setTimeout(typeEffect, pauseTime);
        } else if (isDeleting && charIndex === 0) {
          isDeleting = false;
          textIndex = (textIndex + 1) % texts.length;
          setTimeout(typeEffect, 500);
        }
      }

      // Start typing effect when page loads
      window.addEventListener("DOMContentLoaded", typeEffect);
    </script>
  </body>
</html>
