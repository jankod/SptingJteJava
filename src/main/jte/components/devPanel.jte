@import hr.ja.st.dev.DebugInfo

@param DebugInfo debug

<style>
  #dev-panel { position: fixed; bottom: 12px; right: 12px; z-index: 9999; font-size: 13px; }
  #dev-panel .card { box-shadow: 0 4px 14px rgba(0,0,0,0.2); min-width: 420px; }
  #dev-panel .small { font-size: 12px; }
  #dev-panel code { background: #f6f8fa; padding: 0 4px; border-radius: 3px; }
  #dev-panel .list { max-height: 180px; overflow: auto; }
  #dev-panel a { text-decoration: none; }
  #dev-fab { position: fixed; bottom: 12px; right: 12px; z-index: 10000; }
  #dev-card { display: none; }
</style>
<button id="dev-fab" class="btn btn-sm btn-primary" title="Toggle Dev Panel" onclick="return __devToggle();">Dev</button>
<div id="dev-panel">
  <div id="dev-card" class="card">
    <div class="card-header d-flex justify-content-between align-items-center">
      <strong>Dev: Request Inspector</strong>
      <div class="d-flex align-items-center gap-2">
        <span class="small text-secondary">${debug.requestUri}</span>
        <button class="btn btn-xs btn-outline-secondary" onclick="return __devToggle();">Close</button>
      </div>
    </div>
    <div class="card-body">
      <div class="mb-2">
        <div class="text-secondary small">Controller</div>
        <div class="d-flex align-items-center gap-2">
          <code class="me-2">${debug.controllerClass}#${debug.controllerMethod}()</code>
          @if (debug.controllerSourcePath != null)
            <a class="btn btn-xs btn-outline-primary" href="#" onclick="return __devOpen('${java.net.URLEncoder.encode(debug.controllerSourcePath, java.nio.charset.StandardCharsets.UTF_8)}');">Open</a>
            <a class="btn btn-xs btn-outline-secondary" href="${hr.ja.st.dev.DevToolbarConfig.buildIdeUrl(debug.controllerSourcePath, debug.controllerSourceLine)}" title="IDE link">IDE</a>
          @endif
        </div>
      </div>
      @if (debug.requiredRoles != null && !debug.requiredRoles.isEmpty())
        <div class="mb-2">
          <div class="text-secondary small">Required Roles</div>
          <div>
            @for(String r : debug.requiredRoles)
              <span class="badge bg-blue-lt me-1">${hr.ja.st.view.Formatters.roleLabel(r)}<span class="ms-1 text-muted">${r}</span></span>
            @endfor
          </div>
        </div>
      @endif
      <div class="mb-2">
        <div class="text-secondary small">View</div>
        <div><code>${debug.viewName != null ? debug.viewName : "(n/a)"}</code></div>
      </div>
      <div>
        <div class="text-secondary small">Templates</div>
        <div class="list">
          @for (String f : debug.templateFiles)
            <div class="d-flex align-items-center gap-2 py-1">
              <a class="btn btn-xs btn-outline-primary" href="#" onclick="return __devOpen('${java.net.URLEncoder.encode(f, java.nio.charset.StandardCharsets.UTF_8)}');" title="Open in IDE">Open</a>
              <a class="btn btn-xs btn-outline-secondary" href="${hr.ja.st.dev.DevToolbarConfig.buildIdeUrl(f, null)}" title="IDE link">IDE</a>
              <div class="small">${f}</div>
            </div>
          @endfor
        </div>
      </div>
    </div>
  </div>
  <div class="text-end small text-secondary mt-1">Only visible in dev</div>
  <script>
    function __devOpen(encodedPath){
      fetch('/__dev/open?path=' + encodedPath, {method:'GET'}).catch(()=>{});
      return false;
    }
    function __devToggle(){
      var card = document.getElementById('dev-card');
      if(!card) return false;
      card.style.display = (card.style.display === 'none' || card.style.display === '') ? 'block' : 'none';
      // add to cookie so it persists
      document.cookie = "devPanelVisible=" + (card.style.display === 'block' ? '1' : '0') + "; path=/";
      return false;
    }
    function __devInit(){
      var matches = document.cookie.match(new RegExp('(?:^|; )' + 'devPanelVisible'.replace(/([.$?*|{}()[]\/+^])/g, '\\$1') + '=([^;]*)'));
      var visible = matches ? decodeURIComponent(matches[1]) === '1' : false;
      if(visible){
        var card = document.getElementById('dev-card');
        if(card) card.style.display = 'block';
      }
    }
    document.addEventListener('DOMContentLoaded', __devInit);
  </script>
</div>
