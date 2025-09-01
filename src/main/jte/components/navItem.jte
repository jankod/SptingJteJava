@param String activePath
@param String currentPath
@param String label
@param gg.jte.Content icon

<%--<br>--%>
<%--activePath = ${activePath}--%>
<%--<br>--%>
<%--currentPath = ${currentPath}--%>

<li class="nav-item  ${activePath.equals(currentPath) ? "active" : ""}">
    <a class="nav-link" href="${currentPath}">
          <span class="nav-link-icon">
<%--            @template.components.icons.home()--%>
            ${icon}
          </span>
        <span class="nav-link-title"> ${label} </span>
    </a>
</li>
