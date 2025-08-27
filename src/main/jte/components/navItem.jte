@param String activePath
@param String currentPath
@param String label


<li class="nav-item">
  <a class="nav-link ${activePath.startsWith(currentPath) ? "active" : ""}" href="${currentPath}">${label}</a>
</li>
