const adminLoginForm = document.querySelector("#adminLoginForm");
const adminNicknameInput = document.querySelector("#adminNicknameInput");
const adminPasswordInput = document.querySelector("#adminPasswordInput");
const adminLoginMessage = document.querySelector("#adminLoginMessage");
const adminDashboard = document.querySelector("#adminDashboard");
const adminUserTableBody = document.querySelector("#adminUserTableBody");
const adminPostTableBody = document.querySelector("#adminPostTableBody");
const adminCommentTableBody = document.querySelector("#adminCommentTableBody");
const adminReportTableBody = document.querySelector("#adminReportTableBody");
const adminLogTableBody = document.querySelector("#adminLogTableBody");
const adminCommentTitle = document.querySelector("#adminCommentTitle");
const adminPostPreview = document.querySelector("#adminPostPreview");
const adminActionMessage = document.querySelector("#adminActionMessage");
const adminLogoutButton = document.querySelector("#adminLogoutButton");
const refreshAdminButton = document.querySelector("#refreshAdminButton");
const adminUserSearchInput = document.querySelector("#adminUserSearchInput");
const adminPostSearchInput = document.querySelector("#adminPostSearchInput");
const adminPostSearchButton = document.querySelector("#adminPostSearchButton");
const adminCommentSearchInput = document.querySelector("#adminCommentSearchInput");
const adminReportSearchInput = document.querySelector("#adminReportSearchInput");
const adminReportStatusSelect = document.querySelector("#adminReportStatusSelect");
const adminUserCount = document.querySelector("#adminUserCount");
const adminReportCount = document.querySelector("#adminReportCount");
const adminLogCount = document.querySelector("#adminLogCount");
const adminLogRefreshButton = document.querySelector("#adminLogRefreshButton");
const adminTabs = document.querySelectorAll("[data-admin-tab]");
const adminSections = document.querySelectorAll("[data-admin-section]");
const adminNoticeDialog = document.querySelector("#adminNoticeDialog");
const adminNoticeCloseButton = document.querySelector("#adminNoticeCloseButton");
const adminNoticeOkButton = document.querySelector("#adminNoticeOkButton");
const adminNoticeTitle = document.querySelector("#adminNoticeTitle");
const adminNoticeMessage = document.querySelector("#adminNoticeMessage");
const adminBanDialog = document.querySelector("#adminBanDialog");
const adminBanForm = document.querySelector("#adminBanForm");
const adminBanCloseButton = document.querySelector("#adminBanCloseButton");
const adminBanTargetText = document.querySelector("#adminBanTargetText");
const adminBanReasonInput = document.querySelector("#adminBanReasonInput");
const adminBanMessage = document.querySelector("#adminBanMessage");
const adminPostDialog = document.querySelector("#adminPostDialog");
const adminPostDialogCloseButton = document.querySelector("#adminPostDialogCloseButton");
const adminPostDialogTitle = document.querySelector("#adminPostDialogTitle");
const adminPostDialogMeta = document.querySelector("#adminPostDialogMeta");
const adminPostDialogContent = document.querySelector("#adminPostDialogContent");
const adminPostDialogCommentsButton = document.querySelector("#adminPostDialogCommentsButton");
const adminPostDialogDeleteButton = document.querySelector("#adminPostDialogDeleteButton");

let selectedAdminPost = null;
let selectedAdminComments = [];
let pendingBanUser = null;

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function statusLabel(status) {
  return {
    ACTIVE: "활성",
    BANNED: "제재",
    DELETED: "탈퇴",
    RECRUITING: "모집중",
    CLOSED: "마감",
    PENDING: "미처리",
    RESOLVED: "처리완료"
  }[status] || status;
}

function actionLabel(actionType) {
  return {
    USER_BANNED: "회원 제재",
    USER_RESTORED: "제재 해제",
    POST_DELETED: "모집글 삭제",
    COMMENT_DELETED: "댓글 삭제",
    REPORT_RESOLVED: "신고 처리"
  }[actionType] || actionType;
}

function formatAdminDate(value) {
  if (!value) {
    return "-";
  }
  return new Intl.DateTimeFormat("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit"
  }).format(new Date(value));
}

function setActionMessage(message) {
  adminActionMessage.textContent = message || "";
}

function showAdminNotice(title, message) {
  adminNoticeTitle.textContent = title;
  adminNoticeMessage.textContent = message;
  if (typeof adminNoticeDialog.showModal === "function") {
    adminNoticeDialog.showModal();
  }
}

function closeAdminNotice() {
  adminNoticeDialog.close();
}

function openBanDialog(userId, nickname) {
  pendingBanUser = { id: userId, nickname };
  adminBanTargetText.textContent = `${nickname} 계정에 적용할 제재 사유를 입력해주세요.`;
  adminBanReasonInput.value = "";
  adminBanMessage.textContent = "";
  if (typeof adminBanDialog.showModal === "function") {
    adminBanDialog.showModal();
    adminBanReasonInput.focus();
  }
}

function closeBanDialog() {
  adminBanDialog.close();
  pendingBanUser = null;
}

function openPostDialog(post) {
  adminPostDialogDeleteButton.dataset.confirming = "false";
  adminPostDialogDeleteButton.textContent = "삭제";
  adminPostDialogTitle.textContent = post.title;
  adminPostDialogMeta.innerHTML = `
    <div><dt>작성자</dt><dd>${escapeHtml(post.authorNickname)}</dd></div>
    <div><dt>카테고리</dt><dd>${escapeHtml(post.categoryName)}</dd></div>
    <div><dt>상태</dt><dd>${statusLabel(post.recruitmentStatus)}</dd></div>
    <div><dt>조회</dt><dd>${post.viewCount || 0}</dd></div>
  `;
  adminPostDialogContent.textContent = post.content;
  if (typeof adminPostDialog.showModal === "function") {
    adminPostDialog.showModal();
  }
}

function closePostDialog() {
  adminPostDialog.close();
}

async function runAdminAction(button, action) {
  if (button?.disabled) {
    return;
  }
  if (button) {
    button.disabled = true;
  }
  try {
    await action();
  } finally {
    if (button) {
      button.disabled = false;
    }
  }
}

function switchAdminTab(tabName) {
  adminTabs.forEach((tab) => {
    tab.classList.toggle("active", tab.dataset.adminTab === tabName);
  });
  adminSections.forEach((section) => {
    section.classList.toggle("active", section.dataset.adminSection === tabName);
  });
}

function searchParamsFrom(input) {
  const params = new URLSearchParams();
  const keyword = input?.value.trim();
  if (keyword) {
    params.set("keyword", keyword);
  }
  return params;
}

async function loadAdminUsers() {
  const params = searchParamsFrom(adminUserSearchInput);
  const response = await fetch(`/api/admin/users?${params.toString()}`, {
    credentials: "same-origin"
  });

  if (!response.ok) {
    adminDashboard.hidden = true;
    adminLoginForm.hidden = false;
    adminLogoutButton.hidden = true;
    return false;
  }

  const users = await response.json();
  adminDashboard.hidden = false;
  adminLoginForm.hidden = true;
  adminLogoutButton.hidden = false;
  adminUserCount.textContent = `${users.length}명`;
  adminUserTableBody.innerHTML = users.length === 0
    ? `<tr><td colspan="6">검색 결과가 없습니다.</td></tr>`
    : users.map((user) => `
      <tr title="${user.status === "BANNED" && user.banReason ? `제재 사유: ${escapeHtml(user.banReason)}` : ""}">
        <td>${user.id}</td>
        <td>${escapeHtml(user.nickname)}</td>
        <td>${escapeHtml(user.email)}</td>
        <td>${escapeHtml(user.role)}</td>
        <td><span class="badge ${user.status === "BANNED" ? "pink" : "green"}">${statusLabel(user.status)}</span></td>
        <td class="admin-row-actions">
          ${user.role === "ADMIN" ? "<span class=\"admin-muted admin-fixed-action\">슈퍼 계정</span>" : `
            <button class="ghost-button small-button admin-fixed-action" type="button" data-user-status="${user.id}" data-user-nickname="${escapeHtml(user.nickname)}" data-next-status="${user.status === "BANNED" ? "ACTIVE" : "BANNED"}">
              ${user.status === "BANNED" ? "제재 해제" : "제재"}
            </button>
          `}
        </td>
      </tr>
    `).join("");
  return true;
}

async function loadAdminPosts() {
  const params = searchParamsFrom(adminPostSearchInput);
  params.set("page", "0");
  params.set("size", "30");
  params.set("sort", "latest");

  const response = await fetch(`/api/study-posts?${params.toString()}`, {
    credentials: "same-origin"
  });

  if (!response.ok) {
    adminPostTableBody.innerHTML = `<tr><td colspan="6">모집글을 불러오지 못했습니다.</td></tr>`;
    return;
  }

  const data = await response.json();
  const posts = data.content || [];
  adminPostTableBody.innerHTML = posts.length === 0
    ? `<tr><td colspan="6">검색 결과가 없습니다.</td></tr>`
    : posts.map((post) => `
      <tr class="${selectedAdminPost?.id === post.id ? "admin-selected-row" : ""} admin-clickable-row" data-preview-post="${post.id}" title="모집글 미리보기">
        <td>${post.id}</td>
        <td>${escapeHtml(post.title)}</td>
        <td>${escapeHtml(post.authorNickname)}</td>
        <td><span class="badge ${post.recruitmentStatus === "CLOSED" ? "pink" : "green"}">${statusLabel(post.recruitmentStatus)}</span></td>
        <td>${post.commentCount || 0}</td>
        <td class="admin-row-actions">
          <button class="ghost-button small-button admin-fixed-action" type="button" data-load-comments="${post.id}">보기</button>
          <button class="danger-button small-button admin-fixed-action" type="button" data-delete-post="${post.id}">삭제</button>
        </td>
      </tr>
    `).join("");
}

async function selectAdminPost(postId) {
  const response = await fetch(`/api/study-posts/${postId}?increaseView=false`, {
    credentials: "same-origin"
  });
  if (!response.ok) {
    setActionMessage("모집글 정보를 불러오지 못했습니다.");
    return;
  }

  selectedAdminPost = await response.json();
  renderPostPreview(selectedAdminPost);
  await loadAdminComments(selectedAdminPost.id);
  await loadAdminPosts();
  openPostDialog(selectedAdminPost);
}

function renderPostPreview(post) {
  adminPostPreview.innerHTML = `
    <p class="eyebrow">post preview</p>
    <h3>${escapeHtml(post.title)}</h3>
    <dl>
      <div><dt>작성자</dt><dd>${escapeHtml(post.authorNickname)}</dd></div>
      <div><dt>카테고리</dt><dd>${escapeHtml(post.categoryName)}</dd></div>
      <div><dt>상태</dt><dd>${statusLabel(post.recruitmentStatus)}</dd></div>
      <div><dt>조회</dt><dd>${post.viewCount || 0}</dd></div>
    </dl>
    <p>${escapeHtml(post.content).slice(0, 240)}${post.content.length > 240 ? "..." : ""}</p>
  `;
}

async function loadAdminComments(postId = selectedAdminPost?.id) {
  if (!postId) {
    adminCommentTableBody.innerHTML = `<tr><td colspan="5">모집글을 먼저 선택해주세요.</td></tr>`;
    return;
  }

  const response = await fetch(`/api/study-posts/${postId}/comments`, {
    credentials: "same-origin"
  });

  adminCommentTitle.textContent = selectedAdminPost?.title || `모집글 #${postId}`;
  if (!response.ok) {
    adminCommentTableBody.innerHTML = `<tr><td colspan="5">댓글을 불러오지 못했습니다.</td></tr>`;
    return;
  }

  selectedAdminComments = await response.json();
  renderAdminComments();
}

function renderAdminComments() {
  const keyword = adminCommentSearchInput.value.trim().toLowerCase();
  const comments = selectedAdminComments.filter((comment) => {
    const target = `${comment.authorNickname} ${comment.content}`.toLowerCase();
    return !keyword || target.includes(keyword);
  });

  adminCommentTableBody.innerHTML = comments.length === 0
    ? `<tr><td colspan="5">선택한 모집글에 표시할 댓글이 없습니다.</td></tr>`
    : comments.map((comment) => `
      <tr>
        <td>${comment.id}</td>
        <td>${escapeHtml(comment.authorNickname)}</td>
        <td>${escapeHtml(comment.content)}</td>
        <td><span class="badge ${comment.deleted ? "pink" : "green"}">${comment.deleted ? "삭제됨" : "표시중"}</span></td>
        <td class="admin-row-actions">
          ${comment.deleted ? "<span class=\"admin-muted admin-fixed-action\">처리됨</span>" : `<button class="danger-button small-button admin-fixed-action" type="button" data-delete-comment="${comment.id}">삭제</button>`}
        </td>
      </tr>
    `).join("");
}

async function loadAdminReports() {
  const params = searchParamsFrom(adminReportSearchInput);
  if (adminReportStatusSelect.value) {
    params.set("status", adminReportStatusSelect.value);
  }
  const response = await fetch(`/api/admin/reports?${params.toString()}`, {
    credentials: "same-origin"
  });

  if (!response.ok) {
    adminReportTableBody.innerHTML = `<tr><td colspan="6">신고 목록을 불러오지 못했습니다.</td></tr>`;
    return;
  }

  const reports = await response.json();
  adminReportCount.textContent = `${reports.length}건`;
  adminReportTableBody.innerHTML = reports.length === 0
    ? `<tr><td colspan="6">표시할 신고가 없습니다.</td></tr>`
    : reports.map((report) => `
      <tr>
        <td>${report.id}</td>
        <td>
          <strong>${report.targetType === "COMMENT" ? "댓글" : "모집글"}</strong>
          <span class="admin-report-target">${escapeHtml(report.targetType === "COMMENT" ? report.commentContent : report.postTitle)}</span>
        </td>
        <td>${escapeHtml(report.reporterNickname)}</td>
        <td>${escapeHtml(report.reason)}</td>
        <td><span class="badge ${report.status === "PENDING" ? "pink" : "green"}">${statusLabel(report.status)}</span></td>
        <td class="admin-row-actions">
          <button class="ghost-button small-button admin-fixed-action" type="button" data-report-post="${report.postId}">글 보기</button>
          ${report.status === "PENDING" ? `<button class="primary-button small-button admin-fixed-action" type="button" data-resolve-report="${report.id}">처리</button>` : "<span class=\"admin-muted admin-fixed-action\">완료</span>"}
        </td>
      </tr>
    `).join("");
}

async function loadAdminLogs() {
  const response = await fetch("/api/admin/action-logs", {
    credentials: "same-origin"
  });
  if (!response.ok) {
    adminLogTableBody.innerHTML = `<tr><td colspan="5">운영 기록을 불러오지 못했습니다.</td></tr>`;
    return;
  }
  const logs = await response.json();
  adminLogCount.textContent = `${logs.length}건`;
  adminLogTableBody.innerHTML = logs.length === 0
    ? `<tr><td colspan="5">아직 저장된 운영 기록이 없습니다.</td></tr>`
    : logs.map((log) => `
      <tr>
        <td><span class="badge blue">${actionLabel(log.actionType)}</span></td>
        <td>${escapeHtml(log.targetType)} #${log.targetId ?? "-"}</td>
        <td title="${escapeHtml(log.summary)}">${escapeHtml(log.summary)}</td>
        <td>${escapeHtml(log.adminNickname)}</td>
        <td>${formatAdminDate(log.createdAt)}</td>
      </tr>
    `).join("");
}

async function submitAdminLogin(event) {
  event.preventDefault();
  adminLoginMessage.textContent = "";

  const response = await fetch("/api/admin/login", {
    method: "POST",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      nickname: adminNicknameInput.value,
      password: adminPasswordInput.value
    })
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    adminLoginMessage.textContent = data.message || "관리자 로그인을 처리하지 못했습니다.";
    return;
  }

  adminPasswordInput.value = "";
  await loadAdminData();
}

async function loadAdminData() {
  const loggedIn = await loadAdminUsers();
  if (!loggedIn) {
    return;
  }
  await Promise.all([loadAdminPosts(), loadAdminReports(), loadAdminLogs()]);
  if (selectedAdminPost) {
    await loadAdminComments(selectedAdminPost.id);
  }
}

async function updateUserStatus(userId, status, reason = "") {
  setActionMessage("");
  const response = await fetch(`/api/admin/users/${userId}/status`, {
    method: "PATCH",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ status, reason })
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    setActionMessage(data.message || "회원 상태를 변경하지 못했습니다.");
    return;
  }

  setActionMessage("회원 상태를 변경했습니다.");
  await Promise.all([loadAdminUsers(), loadAdminLogs()]);
  showAdminNotice(status === "BANNED" ? "회원 제재 완료" : "제재 해제 완료", "회원 목록을 최신 상태로 갱신했습니다.");
}

async function deleteAdminPost(postId) {
  const response = await fetch(`/api/admin/study-posts/${postId}`, {
    method: "DELETE",
    credentials: "same-origin"
  });

  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    setActionMessage(data.message || "모집글을 삭제하지 못했습니다.");
    return;
  }

  setActionMessage("모집글을 삭제했습니다.");
  if (selectedAdminPost?.id === Number(postId)) {
    selectedAdminPost = null;
    selectedAdminComments = [];
    adminPostPreview.innerHTML = `
      <p class="eyebrow">post preview</p>
      <h3>모집글을 선택해주세요</h3>
      <p>글을 선택하면 제목, 작성자, 내용 일부와 댓글 관리가 함께 연결됩니다.</p>
    `;
    adminCommentTitle.textContent = "모집글을 선택해주세요";
    adminCommentTableBody.innerHTML = "";
  }
  await loadAdminData();
  showAdminNotice("모집글 삭제 완료", "모집글, 댓글, 신고 목록을 최신 상태로 다시 불러왔습니다.");
}

async function deleteAdminComment(commentId) {
  const response = await fetch(`/api/admin/comments/${commentId}`, {
    method: "DELETE",
    credentials: "same-origin"
  });

  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    setActionMessage(data.message || "댓글을 삭제하지 못했습니다.");
    return;
  }

  setActionMessage("댓글을 삭제했습니다.");
  await loadAdminComments();
  await loadAdminPosts();
  await loadAdminReports();
  await loadAdminLogs();
  showAdminNotice("댓글 삭제 완료", "댓글과 관련 목록을 최신 상태로 갱신했습니다.");
}

async function resolveReport(reportId) {
  const response = await fetch(`/api/admin/reports/${reportId}/resolve`, {
    method: "PATCH",
    credentials: "same-origin"
  });

  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    setActionMessage(data.message || "신고 상태를 변경하지 못했습니다.");
    return;
  }

  setActionMessage("신고를 처리완료로 변경했습니다.");
  await Promise.all([loadAdminReports(), loadAdminLogs()]);
  showAdminNotice("신고 처리 완료", "신고 목록을 최신 상태로 갱신했습니다.");
}

async function logoutAdmin() {
  await fetch("/api/auth/logout", {
    method: "POST",
    credentials: "same-origin"
  });
  adminDashboard.hidden = true;
  adminLoginForm.hidden = false;
  adminLogoutButton.hidden = true;
}

function armDeleteButton(button, originalText, nextAction) {
  if (button.dataset.confirming !== "true") {
    button.dataset.confirming = "true";
    button.textContent = "한번 더";
    window.setTimeout(() => {
      button.dataset.confirming = "false";
      button.textContent = originalText;
    }, 1800);
    return;
  }
  runAdminAction(button, nextAction);
}

adminTabs.forEach((tab) => {
  tab.addEventListener("click", () => switchAdminTab(tab.dataset.adminTab));
});
adminNoticeCloseButton.addEventListener("click", closeAdminNotice);
adminNoticeOkButton.addEventListener("click", closeAdminNotice);
adminBanCloseButton.addEventListener("click", closeBanDialog);
adminPostDialogCloseButton.addEventListener("click", closePostDialog);
adminLoginForm.addEventListener("submit", submitAdminLogin);
adminBanForm.addEventListener("submit", (event) => {
  event.preventDefault();
  if (!pendingBanUser) {
    return;
  }
  const reason = adminBanReasonInput.value.trim();
  if (!reason) {
    adminBanMessage.textContent = "제재 사유를 입력해주세요.";
    return;
  }
  runAdminAction(event.submitter, async () => {
    const targetUserId = pendingBanUser.id;
    closeBanDialog();
    await updateUserStatus(targetUserId, "BANNED", reason);
  });
});
refreshAdminButton.addEventListener("click", loadAdminData);
adminLogoutButton.addEventListener("click", logoutAdmin);
adminUserSearchInput.addEventListener("input", () => loadAdminUsers());
adminPostSearchInput.addEventListener("keydown", (event) => {
  if (event.key === "Enter") {
    event.preventDefault();
    loadAdminPosts();
  }
});
adminPostSearchButton.addEventListener("click", loadAdminPosts);
adminCommentSearchInput.addEventListener("input", renderAdminComments);
adminReportSearchInput.addEventListener("input", loadAdminReports);
adminReportStatusSelect.addEventListener("change", loadAdminReports);
adminLogRefreshButton.addEventListener("click", loadAdminLogs);

adminUserTableBody.addEventListener("click", (event) => {
  const button = event.target.closest("[data-user-status]");
  if (!button) {
    return;
  }
  if (button.dataset.nextStatus === "BANNED") {
    openBanDialog(button.dataset.userStatus, button.dataset.userNickname);
    return;
  }
  runAdminAction(button, () => updateUserStatus(button.dataset.userStatus, button.dataset.nextStatus));
});

adminPostTableBody.addEventListener("click", (event) => {
  const postRow = event.target.closest("[data-preview-post]");
  const viewButton = event.target.closest("[data-load-comments]");
  const deleteButton = event.target.closest("[data-delete-post]");
  if (deleteButton) {
    armDeleteButton(deleteButton, "삭제", () => deleteAdminPost(deleteButton.dataset.deletePost));
    return;
  }
  if (viewButton) {
    selectAdminPost(Number(viewButton.dataset.loadComments));
    return;
  }
  if (postRow) {
    selectAdminPost(Number(postRow.dataset.previewPost));
  }
});

adminCommentTableBody.addEventListener("click", (event) => {
  const deleteButton = event.target.closest("[data-delete-comment]");
  if (!deleteButton) {
    return;
  }
  armDeleteButton(deleteButton, "삭제", () => deleteAdminComment(deleteButton.dataset.deleteComment));
});

adminReportTableBody.addEventListener("click", (event) => {
  const postButton = event.target.closest("[data-report-post]");
  if (postButton) {
    switchAdminTab("posts");
    selectAdminPost(Number(postButton.dataset.reportPost));
    return;
  }
  const resolveButton = event.target.closest("[data-resolve-report]");
  if (resolveButton) {
    runAdminAction(resolveButton, () => resolveReport(resolveButton.dataset.resolveReport));
  }
});

adminPostDialogCommentsButton.addEventListener("click", () => {
  if (!selectedAdminPost) {
    return;
  }
  closePostDialog();
  switchAdminTab("comments");
});

adminPostDialogDeleteButton.addEventListener("click", () => {
  if (!selectedAdminPost) {
    return;
  }
  const targetPostId = selectedAdminPost.id;
  armDeleteButton(adminPostDialogDeleteButton, "삭제", async () => {
    closePostDialog();
    await deleteAdminPost(targetPostId);
  });
});

loadAdminData();
