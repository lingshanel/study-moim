const categoryNav = document.querySelector("#categoryNav");
const postList = document.querySelector("#postList");
const postCount = document.querySelector("#postCount");
const searchInput = document.querySelector("#searchInput");
const activeFilterStrip = document.querySelector("#activeFilterStrip");
const activeFilterTitle = document.querySelector("#activeFilterTitle");
const activeFilterDescription = document.querySelector("#activeFilterDescription");
const clearFiltersButton = document.querySelector("#clearFiltersButton");
const authDialog = document.querySelector("#authDialog");
const accountRecoveryDialog = document.querySelector("#accountRecoveryDialog");
const writeDialog = document.querySelector("#writeDialog");
const myPageDialog = document.querySelector("#myPageDialog");
const postRoomDialog = document.querySelector("#postRoomDialog");
const roomCard = document.querySelector(".room-card");
const authForm = document.querySelector("#authForm");
const accountRecoveryForm = document.querySelector("#accountRecoveryForm");
const writeForm = document.querySelector("#writeForm");
const closeAuthDialog = document.querySelector("#closeAuthDialog");
const closeAccountRecoveryDialog = document.querySelector("#closeAccountRecoveryDialog");
const closeWriteDialog = document.querySelector("#closeWriteDialog");
const closeMyPageDialog = document.querySelector("#closeMyPageDialog");
const closePostRoomDialog = document.querySelector("#closePostRoomDialog");
const authMenuButton = document.querySelector("#authMenuButton");
const myPageButton = document.querySelector("#myPageButton");
const logoutButton = document.querySelector("#logoutButton");
const loginTab = document.querySelector("#loginTab");
const signupTab = document.querySelector("#signupTab");
const writeButton = document.querySelector("#writeButton");
const dialogTitle = document.querySelector("#dialogTitle");
const dialogHelp = document.querySelector("#dialogHelp");
const nicknameField = document.querySelector("#nicknameField");
const submitAuth = document.querySelector("#submitAuth");
const formMessage = document.querySelector("#formMessage");
const loginNameField = document.querySelector("#loginNameField");
const loginNameLabel = document.querySelector("#loginNameLabel");
const loginNameInput = document.querySelector("#loginNameInput");
const emailField = document.querySelector("#emailField");
const emailInput = document.querySelector("#emailInput");
const passwordInput = document.querySelector("#passwordInput");
const nicknameInput = document.querySelector("#nicknameInput");
const findNicknameButton = document.querySelector("#findNicknameButton");
const resetPasswordButton = document.querySelector("#resetPasswordButton");
const recoveryTitle = document.querySelector("#recoveryTitle");
const recoveryHelp = document.querySelector("#recoveryHelp");
const recoveryEmailInput = document.querySelector("#recoveryEmailInput");
const recoveryNicknameField = document.querySelector("#recoveryNicknameField");
const recoveryNicknameInput = document.querySelector("#recoveryNicknameInput");
const recoveryCodeField = document.querySelector("#recoveryCodeField");
const recoveryCodeInput = document.querySelector("#recoveryCodeInput");
const recoveryPasswordField = document.querySelector("#recoveryPasswordField");
const recoveryPasswordInput = document.querySelector("#recoveryPasswordInput");
const submitRecoveryButton = document.querySelector("#submitRecoveryButton");
const recoveryLoginButton = document.querySelector("#recoveryLoginButton");
const recoveryMessage = document.querySelector("#recoveryMessage");
const profileName = document.querySelector("#profileName");
const profileText = document.querySelector("#profileText");
const postTitle = document.querySelector("#postTitle");
const postCategory = document.querySelector("#postCategory");
const postStudyType = document.querySelector("#postStudyType");
const postContent = document.querySelector("#postContent");
const postLocation = document.querySelector("#postLocation");
const postMaxMembers = document.querySelector("#postMaxMembers");
const postImage = document.querySelector("#postImage");
const submitPost = document.querySelector("#submitPost");
const postFormMessage = document.querySelector("#postFormMessage");
const prevPage = document.querySelector("#prevPage");
const nextPage = document.querySelector("#nextPage");
const pageInfo = document.querySelector("#pageInfo");
const detailTitle = document.querySelector("#detailTitle");
const detailContent = document.querySelector("#detailContent");
const detailMeta = document.querySelector("#detailMeta");
const commentList = document.querySelector("#commentList");
const commentForm = document.querySelector("#commentForm");
const commentContent = document.querySelector("#commentContent");
const commentMessage = document.querySelector("#commentMessage");
const commentEditBanner = document.querySelector("#commentEditBanner");
const cancelCommentEditButton = document.querySelector("#cancelCommentEditButton");
const commentReplyBanner = document.querySelector("#commentReplyBanner");
const commentReplyText = document.querySelector("#commentReplyText");
const cancelCommentReplyButton = document.querySelector("#cancelCommentReplyButton");
const submitCommentButton = document.querySelector("#submitCommentButton");
const imageDialog = document.querySelector("#imageDialog");
const closeImageDialog = document.querySelector("#closeImageDialog");
const originalImage = document.querySelector("#originalImage");
const confirmDialog = document.querySelector("#confirmDialog");
const confirmTitle = document.querySelector("#confirmTitle");
const confirmMessage = document.querySelector("#confirmMessage");
const cancelConfirmButton = document.querySelector("#cancelConfirmButton");
const confirmActionButton = document.querySelector("#confirmActionButton");
const detailActions = document.querySelector("#detailActions");
const reactionActions = document.querySelector("#reactionActions");
const editPostButton = document.querySelector("#editPostButton");
const deletePostButton = document.querySelector("#deletePostButton");
const likeButton = document.querySelector("#likeButton");
const bookmarkButton = document.querySelector("#bookmarkButton");
const toggleStatusButton = document.querySelector("#toggleStatusButton");
const reportButton = document.querySelector("#reportButton");
const reportDialog = document.querySelector("#reportDialog");
const reportForm = document.querySelector("#reportForm");
const closeReportDialog = document.querySelector("#closeReportDialog");
const reportReasonInput = document.querySelector("#reportReasonInput");
const reportMessage = document.querySelector("#reportMessage");
const reportSuccessDialog = document.querySelector("#reportSuccessDialog");
const closeReportSuccessDialog = document.querySelector("#closeReportSuccessDialog");
const reportSuccessOkButton = document.querySelector("#reportSuccessOkButton");
const reportSuccessTitle = document.querySelector("#reportSuccessTitle");
const reportSuccessMessage = document.querySelector("#reportSuccessMessage");
const writeDialogTitle = document.querySelector("#writeDialogTitle");
const detailAuthorCharacter = document.querySelector("#detailAuthorCharacter");
const avatarName = document.querySelector("#avatarName");
const avatarMood = document.querySelector("#avatarMood");
const myPageSummary = document.querySelector("#myPageSummary");
const myPostsButton = document.querySelector("#myPostsButton");
const bookmarksButton = document.querySelector("#bookmarksButton");
const changePasswordButton = document.querySelector("#changePasswordButton");
const deleteAccountButton = document.querySelector("#deleteAccountButton");
const sideBookmarksButton = document.querySelector("#sideBookmarksButton");
const sideMyPostsButton = document.querySelector("#sideMyPostsButton");
const sideNextFeatureButton = document.querySelector("#sideNextFeatureButton");
const onlineCount = document.querySelector("#onlineCount");
const presenceList = document.querySelector("#presenceList");
const customizeButton = document.querySelector("#customizeButton");
const customizeDialog = document.querySelector("#customizeDialog");
const closeCustomizeDialog = document.querySelector("#closeCustomizeDialog");
const saveCharacterButton = document.querySelector("#saveCharacterButton");
const stageSelect = document.querySelector("#stageSelect");
const hatSelect = document.querySelector("#hatSelect");
const itemSelect = document.querySelector("#itemSelect");
const faceSelect = document.querySelector("#faceSelect");
const motionSelect = document.querySelector("#motionSelect");
const faceColorInput = document.querySelector("#faceColorInput");
const passwordForm = document.querySelector("#passwordForm");
const currentPasswordInput = document.querySelector("#currentPasswordInput");
const newPasswordInput = document.querySelector("#newPasswordInput");
const myPageMessage = document.querySelector("#myPageMessage");
const profileStage = document.querySelector("#profileStage");
const customizeStage = document.querySelector("#customizeStage");

let selectedCategory = "all";
let selectedStudyType = "";
let selectedStatus = "";
let selectedSort = "latest";
let selectedAuthorId = "";
let selectedBookmarkView = false;
let authMode = "login";
let recoveryMode = "find-nickname";
let passwordResetStep = "request-code";
let passwordResetToken = "";
let recoveredNickname = "";
let currentUser = null;
let categories = [];
let posts = [];
let selectedPost = null;
let currentPage = 0;
let totalPages = 1;
let totalElements = 0;
let editingPostId = null;
let editingCommentId = null;
let replyingCommentId = null;
let confirmResolver = null;
let recentCommenters = [];
let onlineMembers = [];
let presenceSocket = null;
let presenceReconnectTimer = null;
let presenceShouldReconnect = false;
let loadedComments = [];
let reactionLocked = false;
let reactionCooldownUntil = 0;
let pendingReplyParentId = null;
let reportTarget = null;
const pageSize = 5;

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function characterStorageKey(user = currentUser) {
  return user ? `studyMoimCharacter:${user.id}` : "studyMoimCharacter:guest";
}

function defaultCharacterFor(user = currentUser) {
  const palettes = [
    { stage: "sky", hat: "cap", item: "wand", face: "smile", motion: "idle", faceColor: "#ffe0bc" },
    { stage: "room", hat: "crown", item: "book", face: "wow", motion: "jump", faceColor: "#ffd8ae" },
    { stage: "night", hat: "beanie", item: "pencil", face: "sleepy", motion: "sleep", faceColor: "#ffe8ca" },
    { stage: "library", hat: "helmet", item: "laptop", face: "cool", motion: "dance", faceColor: "#f7cda6" }
  ];
  const index = user?.id ? Number(user.id) % palettes.length : 0;
  return palettes[index];
}

function characterStyleFor(user = currentUser) {
  let saved = {};
  try {
    saved = JSON.parse(localStorage.getItem(characterStorageKey(user)) || "{}");
  } catch {
    localStorage.removeItem(characterStorageKey(user));
  }
  if (!saved || typeof saved !== "object" || Array.isArray(saved)) {
    saved = {};
  }
  const legacyFaceMap = { angry: "focus" };
  const legacyFace = saved.emotion ? { face: legacyFaceMap[saved.emotion] || saved.emotion } : {};
  const normalizedSaved = saved.face === "angry" ? { ...saved, face: "focus" } : saved;
  return { ...defaultCharacterFor(user), ...legacyFace, ...normalizedSaved };
}

function setCharacterDataset(stageElement, style) {
  if (!stageElement) {
    return;
  }
  stageElement.dataset.characterStage = style.stage;
  stageElement.dataset.characterHat = style.hat;
  stageElement.dataset.characterItem = style.item;
  stageElement.dataset.characterFace = style.face;
  stageElement.dataset.characterMotion = style.motion;
}

function setMainCharacterDataset(style) {
  setCharacterDataset(profileStage, style);
  setCharacterDataset(customizeStage, style);
}

function applyCharacterStyle(user = currentUser) {
  const style = characterStyleFor(user);
  const { stage, hat, item, face, motion, faceColor } = style;
  setMainCharacterDataset(style);
  document.documentElement.style.setProperty("--character-face", faceColor);
  stageSelect.value = stage;
  hatSelect.value = hat;
  itemSelect.value = item;
  faceSelect.value = face;
  motionSelect.value = motion;
  faceColorInput.value = faceColor;
}

function requireLogin(message = "로그인이 필요한 기능입니다.") {
  if (currentUser) {
    return true;
  }
  openAuthDialog("login", message);
  return false;
}

function openConfirmDialog({ title, message, actionText = "확인" }) {
  confirmTitle.textContent = title;
  confirmMessage.textContent = message;
  confirmActionButton.textContent = actionText;
  confirmDialog.showModal();

  return new Promise((resolve) => {
    confirmResolver = resolve;
  });
}

function closeConfirmDialog(result) {
  if (confirmDialog.open) {
    confirmDialog.close();
  }
  if (confirmResolver) {
    confirmResolver(result);
    confirmResolver = null;
  }
}

function playButtonEffect(button, effectName) {
  button.dataset.effect = effectName;
  button.classList.remove("is-reacting");
  void button.offsetWidth;
  button.classList.add("is-reacting");
  window.setTimeout(() => {
    button.classList.remove("is-reacting");
    delete button.dataset.effect;
  }, 520);
}

function setReactionButtonsDisabled(disabled) {
  likeButton.disabled = disabled;
  bookmarkButton.disabled = disabled;
  toggleStatusButton.disabled = disabled;
  reportButton.disabled = disabled;
}

function canUseReactionButton() {
  return !reactionLocked && Date.now() >= reactionCooldownUntil;
}

function beginReactionCooldown() {
  reactionLocked = true;
  reactionCooldownUntil = Date.now() + 700;
  setReactionButtonsDisabled(true);
}

function endReactionCooldown() {
  const remaining = Math.max(0, reactionCooldownUntil - Date.now());
  window.setTimeout(() => {
    reactionLocked = false;
    setReactionButtonsDisabled(false);
  }, remaining);
}

function canScrollVertically(element, deltaY) {
  if (!element || element.scrollHeight <= element.clientHeight) {
    return false;
  }
  const atTop = element.scrollTop <= 0;
  const atBottom = Math.ceil(element.scrollTop + element.clientHeight) >= element.scrollHeight;
  return !((atTop && deltaY < 0) || (atBottom && deltaY > 0));
}

function containRoomWheelScroll(event) {
  let target = event.target instanceof Element ? event.target : roomCard;
  while (target && target !== roomCard) {
    if (canScrollVertically(target, event.deltaY)) {
      event.stopPropagation();
      return;
    }
    target = target.parentElement;
  }

  if (canScrollVertically(roomCard, event.deltaY)) {
    event.stopPropagation();
    return;
  }

  event.preventDefault();
  event.stopPropagation();
}

function characterMarkup(className = "", userId = null) {
  const user = userId ? { id: userId } : currentUser;
  const { stage, hat, item, face, motion, faceColor } = characterStyleFor(user);
  return `
    <div class="character-stage mini-stage ${className}" data-character-stage="${stage}" data-character-hat="${hat}" data-character-item="${item}" data-character-face="${face}" data-character-motion="${motion}" style="--character-face: ${faceColor};">
      <div class="my-character mini">
        <span class="character-face">
          <span class="character-eye left"></span>
          <span class="character-eye right"></span>
          <span class="character-mouth"></span>
        </span>
        <span class="character-hat"></span>
        <span class="character-left-hand"></span>
        <span class="character-right-hand"></span>
        <span class="character-left-foot"></span>
        <span class="character-right-foot"></span>
        <span class="character-item"></span>
      </div>
    </div>
  `;
}

function renderPresencePanel() {
  onlineCount.textContent = `${onlineMembers.length} online`;
  if (onlineMembers.length === 0) {
    presenceList.innerHTML = `<p class="presence-empty">${currentUser ? "온라인 멤버를 확인하는 중입니다." : "로그인하면 접속 멤버를 확인할 수 있습니다."}</p>`;
    return;
  }
  presenceList.innerHTML = onlineMembers.slice(0, 8).map((member) => `
    <article class="presence-member online">
      ${characterMarkup("presence-character", member.id)}
      <div>
        <strong>${escapeHtml(member.nickname)}</strong>
        <span>${currentUser && member.id === currentUser.id ? "나 · 접속 중" : "접속 중"}</span>
      </div>
    </article>
  `).join("");
}

function connectPresence() {
  if (!currentUser || presenceSocket?.readyState === WebSocket.OPEN || presenceSocket?.readyState === WebSocket.CONNECTING) {
    return;
  }
  presenceShouldReconnect = true;
  const protocol = window.location.protocol === "https:" ? "wss:" : "ws:";
  presenceSocket = new WebSocket(`${protocol}//${window.location.host}/ws/presence`);
  presenceSocket.addEventListener("message", (event) => {
    const data = JSON.parse(event.data);
    if (data.type !== "presence" || !Array.isArray(data.members)) {
      return;
    }
    onlineMembers = data.members;
    renderPresencePanel();
  });
  presenceSocket.addEventListener("close", () => {
    presenceSocket = null;
    onlineMembers = [];
    renderPresencePanel();
    if (presenceShouldReconnect && currentUser) {
      window.clearTimeout(presenceReconnectTimer);
      presenceReconnectTimer = window.setTimeout(connectPresence, 2500);
    }
  });
}

function disconnectPresence() {
  presenceShouldReconnect = false;
  window.clearTimeout(presenceReconnectTimer);
  presenceReconnectTimer = null;
  onlineMembers = [];
  const socket = presenceSocket;
  presenceSocket = null;
  if (socket && socket.readyState < WebSocket.CLOSING) {
    socket.close(1000, "logout");
  }
  renderPresencePanel();
}

function updateProfile(user) {
  currentUser = user;
  document.body.classList.add("is-logged-in");
  profileName.textContent = user.nickname;
  profileText.textContent = "내 스터디 활동을 확인하고 캐릭터를 꾸밀 수 있습니다.";
  authMenuButton.hidden = true;
  myPageButton.hidden = false;
  logoutButton.hidden = false;
  customizeButton.hidden = false;
  applyCharacterStyle(user);
  renderPresencePanel();
  connectPresence();
  if (selectedPost) {
    renderDetail(selectedPost);
    renderComments(loadedComments);
  }
}

function clearProfile() {
  disconnectPresence();
  currentUser = null;
  document.body.classList.remove("is-logged-in");
  profileName.textContent = "방문자";
  profileText.textContent = "로그인하면 내 정보가 여기에 표시됩니다.";
  authMenuButton.hidden = false;
  myPageButton.hidden = true;
  logoutButton.hidden = true;
  customizeButton.hidden = true;
  applyCharacterStyle(null);
  renderPresencePanel();
  if (selectedPost) {
    renderDetail(selectedPost);
    renderComments(loadedComments);
  }
}

function studyTypeLabel(value) {
  return {
    ONLINE: "온라인",
    OFFLINE: "오프라인",
    HYBRID: "혼합"
  }[value] || value;
}

function statusLabel(value) {
  return {
    RECRUITING: "모집중",
    CLOSED: "마감"
  }[value] || value;
}

function categoryNameFor(slug) {
  if (slug === "all") {
    return "전체";
  }
  return categories.find((category) => category.slug === slug)?.name || slug;
}

function hasActiveExploreFilter() {
  return selectedCategory !== "all"
    || selectedStudyType
    || selectedStatus
    || selectedSort !== "latest"
    || selectedAuthorId
    || selectedBookmarkView
    || searchInput.value.trim();
}

function renderExploreState() {
  const parts = [];
  const keyword = searchInput.value.trim();

  if (selectedAuthorId && currentUser && String(currentUser.id) === selectedAuthorId) {
    parts.push("내가 쓴 글");
  }
  if (selectedBookmarkView) {
    parts.push("내 북마크");
  }
  if (selectedCategory !== "all") {
    parts.push(`${categoryNameFor(selectedCategory)} 카테고리`);
  }
  if (selectedStudyType) {
    parts.push(`${studyTypeLabel(selectedStudyType)} 스터디`);
  }
  if (selectedStatus) {
    parts.push(statusLabel(selectedStatus));
  }
  if (selectedSort === "popular") {
    parts.push("인기순");
  }
  if (keyword) {
    parts.push(`"${keyword}" 검색`);
  }

  activeFilterStrip.hidden = !hasActiveExploreFilter();
  activeFilterTitle.textContent = parts.length > 0 ? parts.join(" · ") : "전체 모집글";
  activeFilterDescription.textContent = parts.length > 0
    ? "현재 조건으로 모집글을 보고 있습니다."
    : "모든 스터디 모집글을 보고 있습니다.";
}

function resetExploreState() {
  selectedCategory = "all";
  selectedStudyType = "";
  selectedStatus = "";
  selectedSort = "latest";
  selectedAuthorId = "";
  selectedBookmarkView = false;
  searchInput.value = "";
  currentPage = 0;
  renderCategoryNav();
  loadPosts();
}

function renderCategoryNav() {
  const categoryButtons = categories.map((category) => `
    <button class="category" data-category="${category.slug}">${category.name}</button>
  `).join("");

  categoryNav.innerHTML = `
    <button class="category ${selectedCategory === "all" ? "active" : ""}" data-category="all">전체</button>
    ${categoryButtons}
  `;

  categoryNav.querySelectorAll(".category").forEach((button) => {
    if (button.dataset.category === selectedCategory) {
      button.classList.add("active");
    }

    button.addEventListener("click", () => {
      selectedCategory = button.dataset.category;
      selectedStudyType = "";
      selectedStatus = "";
      selectedSort = "latest";
      selectedAuthorId = "";
      selectedBookmarkView = false;
      currentPage = 0;
      renderCategoryNav();
      loadPosts();
    });
  });
}

function renderCategoryOptions() {
  if (categories.length === 0) {
    return;
  }

  postCategory.innerHTML = categories.map((category) => `
    <option value="${category.slug}">${category.name}</option>
  `).join("");
}

function renderPosts(items) {
  postCount.textContent = `${totalElements || items.length}개`;

  if (items.length === 0) {
    const emptyTitle = selectedBookmarkView
      ? "아직 북마크한 모집글이 없습니다."
      : hasActiveExploreFilter() ? "조건에 맞는 모집글이 없습니다." : "아직 모집글이 없습니다.";
    const emptyDescription = selectedBookmarkView
      ? "관심 있는 모집글에서 북마크를 눌러두면 여기에 모입니다."
      : hasActiveExploreFilter() ? "검색어나 필터를 조금 넓혀서 다시 찾아보세요." : "첫 스터디 모집글을 작성해보세요.";
    postList.innerHTML = `
      <article class="empty-card">
        <span class="empty-icon">?</span>
        <h3>${emptyTitle}</h3>
        <p>${emptyDescription}</p>
      </article>
    `;
    return;
  }

  postList.innerHTML = items.map((post) => `
    <article class="post-card ${selectedPost?.id === post.id ? "selected" : ""} ${post.recruitmentStatus === "CLOSED" ? "closed" : ""} ${post.id ? "" : "sample"}" data-post-id="${post.id || ""}" ${post.id ? 'tabindex="0"' : ""}>
      ${post.imageUrl ? `<img class="post-thumb" src="${escapeHtml(post.imageUrl)}" alt="">` : ""}
      <div class="post-meta">
        <span class="badge">${escapeHtml(post.categoryName)}</span>
        <span class="badge green">${studyTypeLabel(post.studyType)}</span>
        <span class="badge pink">${statusLabel(post.recruitmentStatus)}</span>
      </div>
      <h3>${escapeHtml(post.title)}</h3>
      <p>${escapeHtml(post.content)}</p>
      <div class="post-tags">
        <span>작성자 ${escapeHtml(post.authorNickname)}</span>
        <span>댓글 ${post.commentCount || 0}</span>
        <span>조회 ${post.viewCount}</span>
        <span>북마크 ${post.bookmarkCount}</span>
      </div>
    </article>
  `).join("");

  postList.querySelectorAll(".post-card[data-post-id]").forEach((card) => {
    const openCard = () => {
      const postId = Number(card.dataset.postId);
      if (postId) {
        selectPost(postId);
      }
    };
    card.addEventListener("click", openCard);
    card.addEventListener("keydown", (event) => {
      if (event.key === "Enter" || event.key === " ") {
        event.preventDefault();
        openCard();
      }
    });
  });
}

function resetWriteForm() {
  editingPostId = null;
  writeDialogTitle.textContent = "스터디 모집글 작성";
  submitPost.textContent = "등록";
  postTitle.value = "";
  postContent.value = "";
  postLocation.value = "";
  postMaxMembers.value = "5";
  postImage.value = "";
  if (categories.length > 0) {
    postCategory.value = categories[0].slug;
  }
  postStudyType.value = "ONLINE";
}

function renderPagination() {
  pageInfo.textContent = `${currentPage + 1} / ${Math.max(totalPages, 1)}`;
  prevPage.disabled = currentPage <= 0;
  nextPage.disabled = currentPage >= totalPages - 1;
}

function renderPostLoadError() {
  postCount.textContent = "불러오기 실패";
  postList.innerHTML = `
    <article class="empty-card error-card">
      <span class="empty-icon">!</span>
      <h3>모집글을 불러오지 못했습니다.</h3>
      <p>서버 연결을 확인한 뒤 잠시 후 다시 시도해주세요.</p>
      <button type="button" class="secondary-button" id="retryPostsButton">다시 불러오기</button>
    </article>
  `;
  document.querySelector("#retryPostsButton")?.addEventListener("click", loadPosts);
}

async function loadCategories() {
  try {
    const response = await fetch("/api/categories", {
      credentials: "same-origin"
    });
    if (!response.ok) {
      throw new Error("categories failed");
    }
    categories = await response.json();
  } catch (error) {
    categories = [
      { name: "백엔드", slug: "backend" },
      { name: "프론트엔드", slug: "frontend" },
      { name: "CS", slug: "cs" },
      { name: "자격증", slug: "certificate" },
      { name: "면접", slug: "interview" }
    ];
  }

  renderCategoryNav();
  renderCategoryOptions();
}

async function loadPosts() {
  renderExploreState();

  const params = new URLSearchParams();
  const keyword = searchInput.value.trim();

  if (selectedCategory !== "all") {
    params.set("category", selectedCategory);
  }
  if (keyword) {
    params.set("keyword", keyword);
  }
  if (selectedStudyType) {
    params.set("studyType", selectedStudyType);
  }
  if (selectedStatus) {
    params.set("status", selectedStatus);
  }
  if (selectedSort !== "latest") {
    params.set("sort", selectedSort);
  }
  if (selectedAuthorId) {
    params.set("authorId", selectedAuthorId);
  }
  params.set("page", String(currentPage));
  params.set("size", String(pageSize));

  try {
    const response = await fetch(`/api/study-posts?${params.toString()}`, {
      credentials: "same-origin"
    });
    if (!response.ok) {
      throw new Error("posts failed");
    }
    const data = await response.json();
    posts = data.content || [];
    totalElements = data.totalElements || posts.length;
    totalPages = data.totalPages || 1;
    renderPosts(posts);
    renderPagination();
  } catch (error) {
    posts = [];
    totalElements = 0;
    totalPages = 1;
    renderPagination();
    renderPostLoadError();
  }
}

async function selectPost(postId, options = {}) {
  const increaseView = options.increaseView !== false;
  commentMessage.textContent = "";
  resetCommentForm();

  const response = await fetch(`/api/study-posts/${postId}?increaseView=${increaseView}`, {
    credentials: "same-origin"
  });

  if (!response.ok) {
    return;
  }

  selectedPost = await response.json();
  renderDetail(selectedPost);
  renderPosts(posts);
  await loadComments(postId);
  if (!postRoomDialog.open) {
    postRoomDialog.showModal();
  }
}

function renderDetail(post) {
  detailTitle.textContent = post.title;
  detailContent.innerHTML = `
    ${post.imageUrl ? `
      <button class="image-open-button" type="button" data-original-image="${escapeHtml(post.imageUrl)}" aria-label="대표 이미지 원본 보기">
        <img class="detail-image" src="${escapeHtml(post.imageUrl)}" alt="모집글 대표 이미지">
        <span>이미지 크게 보기</span>
      </button>
    ` : ""}
    <span>${escapeHtml(post.content)}</span>
  `;
  detailAuthorCharacter.innerHTML = characterMarkup("room-author-character", post.authorId);
  avatarName.textContent = post.authorNickname;
  avatarMood.textContent = `${post.authorNickname} 님이 ${post.categoryName} 스터디를 모집 중입니다.`;
  detailMeta.innerHTML = `
    <span>작성자 ${escapeHtml(post.authorNickname)}</span>
    <span>${escapeHtml(post.categoryName)}</span>
    <span>${studyTypeLabel(post.studyType)}</span>
    <span>모집 인원 ${post.maxMembers || "-"}명</span>
  `;
  reactionActions.hidden = false;
  likeButton.textContent = `${post.likedByMe ? "♥ 좋아요 취소" : "♡ 좋아요"} ${post.likeCount || 0}`;
  bookmarkButton.textContent = `${post.bookmarkedByMe ? "★ 북마크 취소" : "☆ 북마크"} ${post.bookmarkCount || 0}`;
  likeButton.classList.add("like-reaction");
  bookmarkButton.classList.add("bookmark-reaction");
  likeButton.classList.toggle("reaction-active", post.likedByMe);
  bookmarkButton.classList.toggle("reaction-active", post.bookmarkedByMe);
  toggleStatusButton.hidden = !(currentUser && currentUser.id === post.authorId);
  toggleStatusButton.textContent = post.recruitmentStatus === "RECRUITING" ? "■ 모집 마감" : "▶ 다시 모집";
  toggleStatusButton.classList.toggle("status-closed", post.recruitmentStatus === "CLOSED");
  reportButton.hidden = !(currentUser && currentUser.id !== post.authorId);
  detailActions.hidden = !(currentUser && currentUser.id === post.authorId);

  detailContent.querySelector("[data-original-image]")?.addEventListener("click", (event) => {
    openOriginalImage(event.currentTarget.dataset.originalImage);
  });
}

async function loadComments(postId) {
  const response = await fetch(`/api/study-posts/${postId}/comments`, {
    credentials: "same-origin"
  });

  if (!response.ok) {
    commentList.innerHTML = `<p class="empty-text">댓글을 불러오지 못했습니다.</p>`;
    return;
  }

  loadedComments = await response.json();
  recentCommenters = loadedComments
    .filter((comment) => !comment.deleted)
    .reduce((accumulator, comment) => {
      if (!accumulator.some((item) => item.id === comment.authorId)) {
        accumulator.push({ id: comment.authorId, nickname: comment.authorNickname });
      }
      return accumulator;
    }, []);
  renderPresencePanel();
  renderComments(loadedComments);
  focusPendingReply();
}

function renderComments(comments) {
  if (comments.length === 0) {
    commentList.innerHTML = `<p class="empty-text">아직 댓글이 없습니다.</p>`;
    return;
  }

  const repliesByParent = comments.reduce((accumulator, comment) => {
    if (comment.parentCommentId) {
      accumulator[comment.parentCommentId] = [...(accumulator[comment.parentCommentId] || []), comment];
    }
    return accumulator;
  }, {});
  const rootComments = comments.filter((comment) => !comment.parentCommentId);
  const renderCommentCard = (comment, isReply = false) => `
    <article class="comment-card ${isReply ? "reply" : ""} ${comment.deleted ? "deleted" : ""} ${editingCommentId === comment.id ? "editing" : ""}">
      <div class="comment-avatar">
        ${characterMarkup("", comment.authorId)}
      </div>
      <div class="comment-body">
        <div class="comment-head">
          <strong>${escapeHtml(comment.authorNickname)}</strong>
          <span>${comment.deleted ? "삭제됨" : isReply ? "답글" : "댓글"}</span>
        </div>
        <p>${escapeHtml(comment.content)}</p>
        ${!comment.deleted ? `
          <div class="comment-actions">
            ${!isReply ? `<button class="ghost-button" type="button" data-comment-reply="${comment.id}" data-comment-author="${escapeHtml(comment.authorNickname)}">답글</button>` : ""}
            ${currentUser && currentUser.id !== comment.authorId ? `
              <button class="ghost-button" type="button" data-comment-report="${comment.id}" data-comment-author="${escapeHtml(comment.authorNickname)}">신고</button>
            ` : ""}
            ${currentUser && currentUser.id === comment.authorId ? `
              <button class="ghost-button" type="button" data-comment-edit="${comment.id}" data-comment-content="${escapeHtml(comment.content)}">수정</button>
              <button class="danger-button" type="button" data-comment-delete="${comment.id}">삭제</button>
            ` : ""}
          </div>
        ` : ""}
      </div>
    </article>
  `;

  commentList.innerHTML = rootComments.map((comment) => `
    <div class="comment-thread" data-thread-id="${comment.id}">
      ${renderCommentCard(comment)}
      ${(repliesByParent[comment.id] || []).map((reply) => renderCommentCard(reply, true)).join("")}
    </div>
  `).join("");

  commentList.querySelectorAll("[data-comment-edit]").forEach((button) => {
    button.addEventListener("click", () => startEditComment(Number(button.dataset.commentEdit), button.dataset.commentContent || ""));
  });
  commentList.querySelectorAll("[data-comment-reply]").forEach((button) => {
    button.addEventListener("click", () => startReplyComment(Number(button.dataset.commentReply), button.dataset.commentAuthor || ""));
  });
  commentList.querySelectorAll("[data-comment-delete]").forEach((button) => {
    button.addEventListener("click", () => deleteComment(Number(button.dataset.commentDelete)));
  });
  commentList.querySelectorAll("[data-comment-report]").forEach((button) => {
    button.addEventListener("click", () => openCommentReportDialog(Number(button.dataset.commentReport), button.dataset.commentAuthor || ""));
  });
}

function focusPendingReply() {
  if (!pendingReplyParentId) {
    return;
  }

  const thread = commentList.querySelector(`[data-thread-id="${pendingReplyParentId}"]`);
  if (!thread) {
    pendingReplyParentId = null;
    return;
  }

  const replies = thread.querySelectorAll(".comment-card.reply");
  const target = replies[replies.length - 1] || thread;
  target.classList.add("just-added");
  target.scrollIntoView({ block: "nearest", behavior: "smooth" });
  window.setTimeout(() => target.classList.remove("just-added"), 1200);
  pendingReplyParentId = null;
}

function resetCommentForm() {
  editingCommentId = null;
  replyingCommentId = null;
  commentContent.value = "";
  commentMessage.textContent = "";
  commentEditBanner.hidden = true;
  commentReplyBanner.hidden = true;
  submitCommentButton.textContent = "등록";
  commentContent.placeholder = "스터디에 대해 궁금한 점이나 참여 의사를 남겨주세요.";
  commentList.querySelectorAll(".comment-card.editing").forEach((card) => {
    card.classList.remove("editing");
  });
}

function startEditComment(commentId, content) {
  editingCommentId = commentId;
  replyingCommentId = null;
  commentContent.value = content;
  commentEditBanner.hidden = false;
  commentReplyBanner.hidden = true;
  submitCommentButton.textContent = "수정 완료";
  commentContent.placeholder = "수정할 댓글 내용을 입력해주세요.";
  commentContent.focus();
  renderCommentsFromDomState();
}

function startReplyComment(commentId, authorNickname) {
  if (!requireLogin("답글은 로그인 후 작성할 수 있습니다.")) {
    return;
  }
  editingCommentId = null;
  replyingCommentId = commentId;
  commentContent.value = "";
  commentEditBanner.hidden = true;
  commentReplyBanner.hidden = false;
  commentReplyText.textContent = `${authorNickname} 님에게 답글을 작성하는 중입니다.`;
  submitCommentButton.textContent = "답글 등록";
  commentContent.placeholder = "답글 내용을 입력해주세요.";
  commentContent.focus();
  renderCommentsFromDomState();
}

function renderCommentsFromDomState() {
  commentList.querySelectorAll(".comment-card").forEach((card) => {
    card.classList.remove("editing");
  });
  commentList.querySelector(`[data-comment-edit="${editingCommentId}"]`)?.closest(".comment-card")?.classList.add("editing");
}

function openAuthDialog(mode, message = "") {
  authMode = mode;
  formMessage.textContent = message;
  loginNameInput.value = "";
  emailInput.value = "";
  passwordInput.value = "";
  nicknameInput.value = "";

  if (mode === "signup") {
    signupTab.classList.add("active");
    loginTab.classList.remove("active");
    dialogTitle.textContent = "회원가입";
    dialogHelp.textContent = "닉네임으로 로그인하고 이메일은 계정 확인용으로 남깁니다.";
    loginNameLabel.textContent = "닉네임";
    loginNameField.hidden = true;
    loginNameInput.required = false;
    emailField.hidden = false;
    emailInput.required = true;
    nicknameField.hidden = false;
    submitAuth.textContent = "회원가입";
  } else {
    loginTab.classList.add("active");
    signupTab.classList.remove("active");
    dialogTitle.textContent = "로그인";
    dialogHelp.textContent = "닉네임과 비밀번호로 로그인합니다.";
    loginNameLabel.textContent = "닉네임";
    loginNameField.hidden = false;
    loginNameInput.required = true;
    emailField.hidden = true;
    emailInput.required = false;
    nicknameField.hidden = true;
    submitAuth.textContent = "로그인";
  }

  authDialog.showModal();
}

function openAccountRecovery(mode) {
  recoveryMode = mode;
  passwordResetStep = "request-code";
  passwordResetToken = "";
  recoveryEmailInput.value = "";
  recoveryNicknameInput.value = "";
  recoveryCodeInput.value = "";
  recoveryPasswordInput.value = "";
  recoveryEmailInput.disabled = false;
  recoveryNicknameInput.disabled = false;
  submitRecoveryButton.disabled = false;
  recoveryMessage.textContent = "";
  recoveryLoginButton.hidden = true;
  recoveredNickname = "";

  if (mode === "reset-password") {
    recoveryTitle.textContent = "비밀번호 재설정";
    recoveryHelp.textContent = "가입 이메일과 닉네임을 입력하면 6자리 인증번호를 전송합니다.";
    recoveryNicknameField.hidden = false;
    recoveryNicknameInput.required = true;
    recoveryCodeField.hidden = true;
    recoveryCodeInput.required = false;
    recoveryPasswordField.hidden = true;
    recoveryPasswordInput.required = false;
    submitRecoveryButton.textContent = "인증번호 전송";
  } else {
    recoveryTitle.textContent = "아이디 찾기";
    recoveryHelp.textContent = "가입할 때 사용한 이메일을 입력하면 닉네임을 확인할 수 있습니다.";
    recoveryNicknameField.hidden = true;
    recoveryNicknameInput.required = false;
    recoveryCodeField.hidden = true;
    recoveryCodeInput.required = false;
    recoveryPasswordField.hidden = true;
    recoveryPasswordInput.required = false;
    submitRecoveryButton.textContent = "아이디 찾기";
  }

  authDialog.close();
  accountRecoveryDialog.showModal();
}

async function submitAccountRecoveryForm(event) {
  event.preventDefault();
  recoveryMessage.textContent = "";

  const isPasswordReset = recoveryMode === "reset-password";
  const payload = { email: recoveryEmailInput.value };
  recoveredNickname = recoveryNicknameInput.value || "";
  recoveryLoginButton.hidden = true;

  let path = "/api/auth/find-nickname";
  if (isPasswordReset && passwordResetStep === "request-code") {
    path = "/api/auth/password-reset/code";
    payload.nickname = recoveryNicknameInput.value;
  } else if (isPasswordReset && passwordResetStep === "verify-code") {
    path = "/api/auth/password-reset/verify";
    payload.nickname = recoveryNicknameInput.value;
    payload.code = recoveryCodeInput.value;
  } else if (isPasswordReset && passwordResetStep === "confirm-password") {
    path = "/api/auth/password-reset/confirm";
    delete payload.email;
    payload.resetToken = passwordResetToken;
    payload.newPassword = recoveryPasswordInput.value;
  }

  const response = await fetch(path, {
    method: "POST",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  });

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    if (response.status === 404) {
      recoveryMessage.textContent = "계정 찾기 요청을 처리하지 못했습니다. 잠시 후 다시 시도해주세요.";
      return;
    }
    recoveryMessage.textContent = data.message || "계정 정보를 확인하지 못했습니다.";
    return;
  }

  if (isPasswordReset) {
    if (passwordResetStep === "request-code") {
      passwordResetStep = "verify-code";
      recoveryEmailInput.disabled = true;
      recoveryNicknameInput.disabled = true;
      recoveryCodeField.hidden = false;
      recoveryCodeInput.required = true;
      recoveryHelp.textContent = "이메일로 전송된 6자리 인증번호를 5분 안에 입력해주세요.";
      recoveryMessage.textContent = "인증번호를 전송했습니다.";
      submitRecoveryButton.textContent = "인증번호 확인";
      recoveryCodeInput.focus();
      return;
    }
    if (passwordResetStep === "verify-code") {
      passwordResetToken = data.resetToken;
      passwordResetStep = "confirm-password";
      recoveryCodeField.hidden = true;
      recoveryCodeInput.required = false;
      recoveryPasswordField.hidden = false;
      recoveryPasswordInput.required = true;
      recoveryHelp.textContent = "본인 확인이 완료되었습니다. 새 비밀번호를 입력해주세요.";
      recoveryMessage.textContent = "이메일 인증이 완료되었습니다.";
      submitRecoveryButton.textContent = "비밀번호 변경";
      recoveryPasswordInput.focus();
      return;
    }
    recoveryMessage.textContent = "비밀번호를 변경했습니다. 새 비밀번호로 로그인해주세요.";
    recoveryPasswordInput.value = "";
    recoveryLoginButton.hidden = false;
    submitRecoveryButton.disabled = true;
    return;
  }

  recoveredNickname = data.nickname;
  recoveryMessage.textContent = `아이디는 ${data.nickname} 입니다.`;
  recoveryLoginButton.hidden = false;
}

function moveRecoveryToLogin() {
  accountRecoveryDialog.close();
  openAuthDialog("login", recoveredNickname ? "찾은 아이디로 로그인해주세요." : "");
  if (recoveredNickname) {
    loginNameInput.value = recoveredNickname;
    passwordInput.focus();
  }
}

async function loadCurrentUser() {
  const response = await fetch("/api/users/me", {
    credentials: "same-origin"
  });

  if (!response.ok) {
    clearProfile();
    return null;
  }

  const user = await response.json();
  updateProfile(user);
  return user;
}

async function submitAuthForm(event) {
  event.preventDefault();
  formMessage.textContent = "";

  const payload = {
    nickname: authMode === "signup" ? nicknameInput.value : loginNameInput.value,
    password: passwordInput.value
  };

  if (authMode === "signup") {
    payload.email = emailInput.value;
  }

  const path = authMode === "signup" ? "/api/auth/signup" : "/api/auth/login";
  const response = await fetch(path, {
    method: "POST",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  });

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    formMessage.textContent = data.message || "요청을 처리하지 못했습니다.";
    return;
  }

  updateProfile(data);
  authDialog.close();
}

async function logout() {
  await fetch("/api/auth/logout", {
    method: "POST",
    credentials: "same-origin"
  });
  selectedAuthorId = "";
  selectedBookmarkView = false;
  clearProfile();
  loadPosts();
}

function openMyPage() {
  if (!currentUser) {
    openAuthDialog("login", "마이페이지는 로그인 후 확인할 수 있습니다.");
    return;
  }

  myPageSummary.textContent = `${currentUser.nickname} 님의 활동 공간입니다.`;
  myPageMessage.textContent = "";
  passwordForm.hidden = true;
  myPageDialog.showModal();
}

async function submitPostForm(event) {
  event.preventDefault();
  postFormMessage.textContent = "";

  const user = currentUser || await loadCurrentUser();
  if (!user) {
    postFormMessage.textContent = "로그인 후 작성할 수 있습니다.";
    return;
  }

  const payload = {
    categorySlug: postCategory.value,
    title: postTitle.value,
    content: postContent.value,
    studyType: postStudyType.value,
    location: postLocation.value,
    maxMembers: Number(postMaxMembers.value)
  };

  const path = editingPostId ? `/api/study-posts/${editingPostId}` : "/api/study-posts";
  const response = await fetch(path, {
    method: editingPostId ? "PATCH" : "POST",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  });

  let data = await response.json().catch(() => ({}));

  if (!response.ok) {
    postFormMessage.textContent = data.message || "로그인 후 작성할 수 있습니다.";
    return;
  }

  if (postImage.files.length > 0) {
    const formData = new FormData();
    formData.append("image", postImage.files[0]);
    const imageResponse = await fetch(`/api/study-posts/${data.id}/image`, {
      method: "POST",
      credentials: "same-origin",
      body: formData
    });
    data = await imageResponse.json().catch(() => data);
    if (!imageResponse.ok) {
      postFormMessage.textContent = data.message || "이미지를 업로드하지 못했습니다.";
      return;
    }
  }

  resetWriteForm();
  writeDialog.close();
  await loadPosts();
  if (selectedPost) {
    await selectPost(selectedPost.id, { increaseView: false });
  }
}

async function submitCommentForm(event) {
  event.preventDefault();
  commentMessage.textContent = "";

  const user = currentUser || await loadCurrentUser();
  if (!user) {
    commentMessage.textContent = "로그인 후 댓글을 작성할 수 있습니다.";
    openAuthDialog("login", "댓글은 로그인 후 작성할 수 있습니다.");
    return;
  }

  if (!selectedPost) {
    commentMessage.textContent = "댓글을 작성할 모집글을 먼저 선택해주세요.";
    return;
  }

  const parentBeforeSubmit = replyingCommentId;
  const commentPayload = editingCommentId
    ? { content: commentContent.value }
    : { parentCommentId: replyingCommentId, content: commentContent.value };

  const response = await fetch(editingCommentId ? `/api/comments/${editingCommentId}` : `/api/study-posts/${selectedPost.id}/comments`, {
    method: editingCommentId ? "PATCH" : "POST",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(commentPayload)
  });

  const data = await response.json().catch(() => ({}));

  if (!response.ok) {
    commentMessage.textContent = data.message || (editingCommentId ? "댓글을 수정하지 못했습니다." : "댓글을 등록하지 못했습니다.");
    return;
  }

  pendingReplyParentId = parentBeforeSubmit;
  resetCommentForm();
  await loadComments(selectedPost.id);
  await loadPosts();
}

function openEditPostDialog() {
  if (!selectedPost) {
    return;
  }

  editingPostId = selectedPost.id;
  writeDialogTitle.textContent = "스터디 모집글 수정";
  submitPost.textContent = "수정";
  postTitle.value = selectedPost.title;
  postCategory.value = selectedPost.categorySlug;
  postStudyType.value = selectedPost.studyType;
  postContent.value = selectedPost.content;
  postLocation.value = selectedPost.location || "";
  postMaxMembers.value = selectedPost.maxMembers || 5;
  postImage.value = "";
  postFormMessage.textContent = "";
  writeDialog.showModal();
}

async function deleteSelectedPost() {
  if (!selectedPost) {
    return;
  }

  const confirmed = await openConfirmDialog({
    title: "모집글 삭제",
    message: "삭제한 모집글은 되돌릴 수 없습니다. 정말 삭제할까요?",
    actionText: "삭제"
  });
  if (!confirmed) {
    return;
  }

  const response = await fetch(`/api/study-posts/${selectedPost.id}`, {
    method: "DELETE",
    credentials: "same-origin"
  });

  if (!response.ok) {
    commentMessage.textContent = "모집글을 삭제하지 못했습니다.";
    return;
  }

  selectedPost = null;
  resetCommentForm();
  detailAuthorCharacter.innerHTML = "";
  avatarName.textContent = "스터디모임";
  avatarMood.textContent = "모집글을 선택하면 작성자와 댓글을 확인할 수 있습니다.";
  detailTitle.textContent = "모집글을 선택해주세요";
  detailContent.textContent = "왼쪽 모집글을 클릭하면 상세 내용과 댓글을 볼 수 있습니다.";
  detailMeta.innerHTML = "";
  reactionActions.hidden = true;
  detailActions.hidden = true;
  commentList.innerHTML = `<p class="empty-text">아직 선택한 모집글이 없습니다.</p>`;
  postRoomDialog.close();
  await loadPosts();
}

async function deleteComment(commentId) {
  const confirmed = await openConfirmDialog({
    title: "댓글 삭제",
    message: "이 댓글을 삭제할까요? 답글 흐름에서는 삭제된 댓글로 표시됩니다.",
    actionText: "삭제"
  });
  if (!confirmed) {
    return;
  }

  const response = await fetch(`/api/comments/${commentId}`, {
    method: "DELETE",
    credentials: "same-origin"
  });

  if (response.ok && selectedPost) {
    if (editingCommentId === commentId) {
      resetCommentForm();
    }
    await loadComments(selectedPost.id);
    await loadPosts();
  }
}

async function toggleLike() {
  if (!selectedPost || !canUseReactionButton()) {
    return;
  }
  const user = currentUser || await loadCurrentUser();
  if (!user) {
    openAuthDialog("login", "좋아요는 로그인 후 사용할 수 있습니다.");
    return;
  }

  const liked = selectedPost.likedByMe;
  beginReactionCooldown();
  playButtonEffect(likeButton, liked ? "취소" : "좋아요");
  try {
    const response = await fetch(`/api/study-posts/${selectedPost.id}/likes`, {
      method: liked ? "DELETE" : "POST",
      credentials: "same-origin"
    });
    if (!response.ok) {
      commentMessage.textContent = "좋아요 상태를 변경하지 못했습니다.";
      return;
    }
    await selectPost(selectedPost.id, { increaseView: false });
    await loadPosts();
  } finally {
    endReactionCooldown();
  }
}

async function toggleBookmark() {
  if (!selectedPost || !canUseReactionButton()) {
    return;
  }
  const user = currentUser || await loadCurrentUser();
  if (!user) {
    openAuthDialog("login", "북마크는 로그인 후 사용할 수 있습니다.");
    return;
  }

  const bookmarked = selectedPost.bookmarkedByMe;
  beginReactionCooldown();
  playButtonEffect(bookmarkButton, bookmarked ? "해제" : "저장");
  try {
    const response = await fetch(`/api/study-posts/${selectedPost.id}/bookmarks`, {
      method: bookmarked ? "DELETE" : "POST",
      credentials: "same-origin"
    });
    if (!response.ok) {
      commentMessage.textContent = "북마크 상태를 변경하지 못했습니다.";
      return;
    }
    await selectPost(selectedPost.id, { increaseView: false });
    await loadPosts();
  } finally {
    endReactionCooldown();
  }
}

async function toggleRecruitmentStatus() {
  if (!selectedPost || !canUseReactionButton()) {
    return;
  }

  const nextStatus = selectedPost.recruitmentStatus === "RECRUITING" ? "CLOSED" : "RECRUITING";
  beginReactionCooldown();
  playButtonEffect(toggleStatusButton, nextStatus === "CLOSED" ? "마감" : "모집");
  try {
    const response = await fetch(`/api/study-posts/${selectedPost.id}/status`, {
      method: "PATCH",
      credentials: "same-origin",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        recruitmentStatus: nextStatus
      })
    });

    if (response.ok) {
      await selectPost(selectedPost.id, { increaseView: false });
      await loadPosts();
    } else {
      commentMessage.textContent = "모집 상태를 변경하지 못했습니다.";
    }
  } finally {
    endReactionCooldown();
  }
}

async function openReportDialog() {
  if (!selectedPost) {
    return;
  }
  const user = currentUser || await loadCurrentUser();
  if (!user) {
    openAuthDialog("login", "신고는 로그인 후 접수할 수 있습니다.");
    return;
  }
  if (user.id === selectedPost.authorId) {
    commentMessage.textContent = "본인이 작성한 모집글은 신고할 수 없습니다.";
    return;
  }

  reportTarget = { type: "post", id: selectedPost.id };
  reportReasonInput.value = "";
  reportMessage.textContent = "";
  reportDialog.querySelector("h2").textContent = "모집글 신고";
  reportDialog.querySelector("p").textContent = "관리자가 확인할 수 있도록 게시글 신고 사유를 남겨주세요.";
  reportDialog.showModal();
}

async function openCommentReportDialog(commentId, authorNickname) {
  const user = currentUser || await loadCurrentUser();
  if (!user) {
    openAuthDialog("login", "댓글 신고는 로그인 후 접수할 수 있습니다.");
    return;
  }

  reportTarget = { type: "comment", id: commentId };
  reportReasonInput.value = "";
  reportMessage.textContent = "";
  reportDialog.querySelector("h2").textContent = `${authorNickname || "댓글"} 님의 댓글 신고`;
  reportDialog.querySelector("p").textContent = "관리자가 확인할 수 있도록 댓글 신고 사유를 남겨주세요.";
  reportDialog.showModal();
}

async function submitReport(event) {
  event.preventDefault();
  if (!reportTarget) {
    return;
  }

  reportMessage.textContent = "";
  const path = reportTarget.type === "comment"
    ? `/api/comments/${reportTarget.id}/reports`
    : `/api/study-posts/${reportTarget.id}/reports`;
  const response = await fetch(path, {
    method: "POST",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ reason: reportReasonInput.value })
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    reportMessage.textContent = data.message || "신고를 접수하지 못했습니다.";
    return;
  }

  const completedType = reportTarget.type;
  reportDialog.close();
  reportTarget = null;
  showReportSuccess(completedType);
}

function showReportSuccess(type) {
  reportSuccessTitle.textContent = type === "comment" ? "댓글 신고가 접수되었습니다" : "게시글 신고가 접수되었습니다";
  reportSuccessMessage.textContent = "운영자가 신고 내용을 확인하고 필요한 조치를 검토합니다.";
  reportSuccessDialog.showModal();
}

function showPasswordForm() {
  passwordForm.hidden = false;
  myPageMessage.textContent = "";
  currentPasswordInput.value = "";
  newPasswordInput.value = "";
  currentPasswordInput.focus();
}

function openCustomizeDialog() {
  if (!requireLogin("캐릭터 꾸미기는 로그인 후 사용할 수 있습니다.")) {
    return;
  }
  applyCharacterStyle();
  customizeDialog.showModal();
}

function openOriginalImage(imageUrl) {
  originalImage.src = imageUrl;
  imageDialog.showModal();
}

function closeOriginalImage() {
  imageDialog.close();
  originalImage.src = "";
}

function saveCharacter() {
  localStorage.setItem(characterStorageKey(), JSON.stringify({
    stage: stageSelect.value,
    hat: hatSelect.value,
    item: itemSelect.value,
    face: faceSelect.value,
    motion: motionSelect.value,
    faceColor: faceColorInput.value
  }));
  applyCharacterStyle();
  customizeDialog.close();
}

async function submitPasswordForm(event) {
  event.preventDefault();
  myPageMessage.textContent = "";
  const response = await fetch("/api/users/me/password", {
    method: "PATCH",
    credentials: "same-origin",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      currentPassword: currentPasswordInput.value,
      newPassword: newPasswordInput.value
    })
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    myPageMessage.textContent = data.message || "비밀번호 변경에 실패했습니다.";
    return;
  }
  passwordForm.hidden = true;
  myPageMessage.textContent = "비밀번호를 변경했습니다.";
}

async function deleteAccount() {
  const confirmed = await openConfirmDialog({
    title: "회원 탈퇴",
    message: "회원 탈퇴를 진행할까요? 작성한 활동 정보는 일부 남을 수 있습니다.",
    actionText: "탈퇴"
  });
  if (!confirmed) {
    return;
  }

  const response = await fetch("/api/users/me", {
    method: "DELETE",
    credentials: "same-origin"
  });

  if (response.ok) {
    myPageDialog.close();
    clearProfile();
  } else {
    myPageMessage.textContent = "회원 탈퇴를 처리하지 못했습니다.";
  }
}

searchInput.addEventListener("input", () => {
  selectedAuthorId = "";
  selectedBookmarkView = false;
  currentPage = 0;
  renderExploreState();
  loadPosts();
});
clearFiltersButton.addEventListener("click", resetExploreState);
authMenuButton.addEventListener("click", () => openAuthDialog("login"));
myPageButton.addEventListener("click", openMyPage);
logoutButton.addEventListener("click", logout);
window.addEventListener("beforeunload", disconnectPresence);
loginTab.addEventListener("click", () => openAuthDialog("login"));
signupTab.addEventListener("click", () => openAuthDialog("signup"));
findNicknameButton.addEventListener("click", () => openAccountRecovery("find-nickname"));
resetPasswordButton.addEventListener("click", () => openAccountRecovery("reset-password"));
recoveryLoginButton.addEventListener("click", moveRecoveryToLogin);
writeButton.addEventListener("click", () => {
  if (!requireLogin("글쓰기는 로그인 후 사용할 수 있습니다.")) {
    return;
  }
  resetWriteForm();
  postFormMessage.textContent = "";
  writeDialog.showModal();
});
authForm.addEventListener("submit", submitAuthForm);
accountRecoveryForm.addEventListener("submit", submitAccountRecoveryForm);
writeForm.addEventListener("submit", submitPostForm);
commentForm.addEventListener("submit", submitCommentForm);
cancelCommentEditButton.addEventListener("click", resetCommentForm);
cancelCommentReplyButton.addEventListener("click", resetCommentForm);
roomCard.addEventListener("wheel", containRoomWheelScroll, { passive: false });
cancelConfirmButton.addEventListener("click", () => closeConfirmDialog(false));
confirmActionButton.addEventListener("click", () => closeConfirmDialog(true));
confirmDialog.addEventListener("click", (event) => {
  if (event.target === confirmDialog) {
    closeConfirmDialog(false);
  }
});
confirmDialog.addEventListener("cancel", (event) => {
  event.preventDefault();
  closeConfirmDialog(false);
});
closeAuthDialog.addEventListener("click", () => authDialog.close());
closeAccountRecoveryDialog.addEventListener("click", () => accountRecoveryDialog.close());
closeWriteDialog.addEventListener("click", () => writeDialog.close());
closeMyPageDialog.addEventListener("click", () => myPageDialog.close());
closePostRoomDialog.addEventListener("click", () => {
  resetCommentForm();
  postRoomDialog.close();
});
closeCustomizeDialog.addEventListener("click", () => customizeDialog.close());
closeImageDialog.addEventListener("click", closeOriginalImage);
closeReportDialog.addEventListener("click", () => reportDialog.close());
closeReportSuccessDialog.addEventListener("click", () => reportSuccessDialog.close());
reportSuccessOkButton.addEventListener("click", () => reportSuccessDialog.close());
reportForm.addEventListener("submit", submitReport);
imageDialog.addEventListener("click", (event) => {
  if (event.target === imageDialog) {
    closeOriginalImage();
  }
});
editPostButton.addEventListener("click", openEditPostDialog);
deletePostButton.addEventListener("click", deleteSelectedPost);
customizeButton.addEventListener("click", openCustomizeDialog);
saveCharacterButton.addEventListener("click", saveCharacter);
stageSelect.addEventListener("change", () => {
  profileStage.dataset.characterStage = stageSelect.value;
  customizeStage.dataset.characterStage = stageSelect.value;
});
hatSelect.addEventListener("change", () => {
  profileStage.dataset.characterHat = hatSelect.value;
  customizeStage.dataset.characterHat = hatSelect.value;
});
itemSelect.addEventListener("change", () => {
  profileStage.dataset.characterItem = itemSelect.value;
  customizeStage.dataset.characterItem = itemSelect.value;
});
faceSelect.addEventListener("change", () => {
  profileStage.dataset.characterFace = faceSelect.value;
  customizeStage.dataset.characterFace = faceSelect.value;
});
motionSelect.addEventListener("change", () => {
  profileStage.dataset.characterMotion = motionSelect.value;
  customizeStage.dataset.characterMotion = motionSelect.value;
});
faceColorInput.addEventListener("input", () => {
  document.documentElement.style.setProperty("--character-face", faceColorInput.value);
});
myPostsButton.addEventListener("click", () => {
  if (!currentUser) {
    openAuthDialog("login", "내가 쓴 글은 로그인 후 확인할 수 있습니다.");
    return;
  }
  selectedCategory = "all";
  selectedStudyType = "";
  selectedStatus = "";
  selectedSort = "latest";
  selectedAuthorId = String(currentUser.id);
  selectedBookmarkView = false;
  searchInput.value = "";
  myPageDialog.close();
  currentPage = 0;
  renderCategoryNav();
  loadPosts();
});
bookmarksButton.addEventListener("click", () => {
  if (!currentUser) {
    openAuthDialog("login", "북마크는 로그인 후 확인할 수 있습니다.");
    return;
  }
  fetch("/api/users/me/bookmarks", {
    credentials: "same-origin"
  })
    .then((response) => response.ok ? response.json() : [])
    .then((items) => {
      selectedCategory = "all";
      selectedStudyType = "";
      selectedStatus = "";
      selectedSort = "latest";
      selectedAuthorId = "";
      selectedBookmarkView = true;
      searchInput.value = "";
      currentPage = 0;
      posts = items;
      totalElements = items.length;
      totalPages = 1;
      renderCategoryNav();
      renderExploreState();
      renderPosts(items);
      renderPagination();
      myPageDialog.close();
    });
});
sideBookmarksButton.addEventListener("click", () => {
  if (!requireLogin("북마크는 로그인 후 확인할 수 있습니다.")) {
    return;
  }
  openMyPage();
  bookmarksButton.click();
});
sideMyPostsButton.addEventListener("click", () => {
  if (!requireLogin("내가 쓴 글은 로그인 후 확인할 수 있습니다.")) {
    return;
  }
  myPostsButton.click();
});
sideNextFeatureButton.addEventListener("click", () => {
  if (!requireLogin("개발 현황은 로그인 후 마이룸에서 확인할 수 있습니다.")) {
    return;
  }
  myPageSummary.textContent = "프로젝트 2 스터디모임은 주요 기능, 관리자·보안 기능, 배포와 문서화까지 완료했습니다.";
  myPageDialog.showModal();
});
document.querySelectorAll("[data-quick-filter]").forEach((button) => {
  button.addEventListener("click", () => {
    const value = button.dataset.quickFilter;
    selectedStudyType = "";
    selectedStatus = "";
    selectedSort = "latest";
    selectedBookmarkView = false;
    searchInput.value = "";

    if (value.startsWith("category:")) {
      selectedCategory = value.replace("category:", "");
    }
    if (value.startsWith("studyType:")) {
      selectedCategory = "all";
      selectedStudyType = value.replace("studyType:", "");
    }
    if (value.startsWith("status:")) {
      selectedCategory = "all";
      selectedStatus = value.replace("status:", "");
    }
    if (value.startsWith("sort:")) {
      selectedCategory = "all";
      selectedSort = value.replace("sort:", "");
    }

    currentPage = 0;
    renderCategoryNav();
    loadPosts();
  });
});
changePasswordButton.addEventListener("click", showPasswordForm);
passwordForm.addEventListener("submit", submitPasswordForm);
deleteAccountButton.addEventListener("click", deleteAccount);
likeButton.addEventListener("click", toggleLike);
bookmarkButton.addEventListener("click", toggleBookmark);
toggleStatusButton.addEventListener("click", toggleRecruitmentStatus);
reportButton.addEventListener("click", openReportDialog);
prevPage.addEventListener("click", () => {
  if (currentPage > 0) {
    currentPage -= 1;
    loadPosts();
  }
});
nextPage.addEventListener("click", () => {
  if (currentPage < totalPages - 1) {
    currentPage += 1;
    loadPosts();
  }
});

applyCharacterStyle();
renderPresencePanel();
loadCategories().then(loadPosts);
loadCurrentUser();

