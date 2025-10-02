<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link
  rel="stylesheet"
  href="${pageContext.request.contextPath}/css/footer.css"
/>

<footer class="footer">
  <div class="container">
    <div class="row">
      <div class="col-12 col-md-4">
        <h3 class="footer__title">iHRM</h3>
        <p class="footer__description">
          Automate attendance, payroll, and performance tracking in one
          platform. Streamline your HR processes with intelligent automation.
        </p>
      </div>
      <div class="col-12 col-md-4">
        <h4 class="footer__heading">Quick Links</h4>
        <ul class="footer__links">
          <li><a href="#">Home</a></li>
          <li><a href="#">About</a></li>
          <li><a href="#">Features</a></li>
          <li><a href="#">Contact us</a></li>
        </ul>
      </div>
      <div class="col-12 col-md-4">
        <h4 class="footer__heading">Contact</h4>
        <ul class="footer__contact">
          <li>
            Email: <span class="copyable" onclick="copyToClipboard('contact@ihrm.com', this)" title="Click to copy">contact@ihrm.com</span>
          </li>
          <li>
            Phone: <span class="copyable" onclick="copyToClipboard('(84) 123-456-789', this)" title="Click to copy">(84) 123-456-789</span>
          </li>
          <li>
            Address: <span class="copyable" onclick="copyToClipboard('Đại Học FPT, Hà Nội', this)" title="Click to copy">Đại Học FPT, Hà Nội</span>
          </li>
        </ul>
      </div>
    </div>
    <div class="footer__bottom">
      <p>&copy; 2025 iHRM. All rights reserved. Crafted by Group 2</p>
    </div>
  </div>
</footer>

<script>
function copyToClipboard(text, element) {
  navigator.clipboard.writeText(text).then(function() {
    // Show copied feedback
    const originalText = element.textContent;
    element.textContent = 'Copied';
    element.style.color = 'var(--color-success)';

    setTimeout(function() {
      element.textContent = originalText;
      element.style.color = '';
    }, 1500);
  }).catch(function(err) {
    console.error('Failed to copy: ', err);
  });
}
</script>
