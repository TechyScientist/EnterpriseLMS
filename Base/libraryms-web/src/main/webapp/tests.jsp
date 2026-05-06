<% String pageName = "", pageTitle = "Test Page"; %>
<%@ include file="assets/include/header.jsp" %>

<style>
    button {
        display: inline-block;
        width: unset;
        margin: 5px;
    }
</style>

<p id="success"><img src="assets/img/check.png" alt="Success"><strong>Success</strong>: Operation completed Successfully</p>
<p id="error"><img src="assets/img/cross.png" alt="Error"><strong>Error</strong>: Error message</p>
<p id="note"><img src="assets/img/note.png" alt="Note"><strong>Note</strong>: Copy (barcode) is already available</p>
<button onclick="playSound('assets/sound/ding.mp3');">Ding</button>
<button onclick="playSound('assets/sound/bonk.mp3');">Bonk</button>
<button onclick="playSound('assets/sound/chime.mp3');">Chime</button>
<button onclick="playSound('assets/sound/twinkle.mp3');">Twinkle</button>

<script>
    function playSound(path) {
        new Audio(path).play();
    }
</script>
<%@ include file="assets/include/footer.jsp" %>