@param String csrfParameterName
@param String csrfToken

@template.layout.base(title = "Odjava",
activePathPage = hr.ja.st.PageController.HOME,
csrfParameterName = csrfParameterName,
csrfToken = csrfToken,
content = @`
    <div class="row justify-content-center mt-5">
        <div class="col-md-6 col-lg-4">
            <div class="card">
                <div class="card-body text-center">
                    <h2 class="card-title mb-3">Odjava</h2>
                    <p class="text-secondary">Odjavljujem vas...</p>
                    <form id="logoutForm" method="post" action="/logout">
                        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}" />
                        <noscript>
                            <button type="submit" class="btn btn-danger">Odjavi se</button>
                        </noscript>
                    </form>
                </div>
            </div>
        </div>
    </div>
`,
scripts = @`
    <script>
        window.addEventListener('DOMContentLoaded', function(){
            const f = document.getElementById('logoutForm');
            if (f) f.submit();
        });
    </script>
`)
