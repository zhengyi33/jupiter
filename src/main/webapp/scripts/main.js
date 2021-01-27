(function () {
    // get all elements
    var oAvatar = document.getElementById('avatar'),
        oWelcomeMsg = document.getElementById('welcome-msg'),
        oLogoutBtn = document.getElementById('logout-link'),
        oLoginBtn = document.getElementById('login-btn'),
        oLoginForm = document.getElementById('login-form'),
        oLoginUsername = document.getElementById('username'),
        oLoginPwd = document.getElementById('password'),
        oLoginFormBtn = document.getElementById('login-form-btn'),
        oLoginErrorField = document.getElementById('login-error'),
        oRegisterBtn = document.getElementById('register-btn'),
        oRegisterForm = document.getElementById('register-form'),
        oRegisterUsername = document.getElementById('register-username'),
        oRegisterPwd = document.getElementById('register-password'),
        oRegisterFirstName = document.getElementById('register-first-name'),
        oRegisterLastName = document.getElementById('register-last-name'),
        oRegisterFormBtn = document.getElementById('register-form-btn'),
        oRegisterResultField = document.getElementById('register-result'),
        oNearbyBtn = document.getElementById('nearby-btn'),
        oFavBtn = document.getElementById('fav-btn'),
        oRecommendBtn = document.getElementById('recommend-btn'),
        oNavBtnBox = document.getElementsByClassName('main-nav')[0],
        oNavBtnList = document.getElementsByClassName('main-nav-btn'),
        oItemNav = document.getElementById('item-nav'),
        oItemList = document.getElementById('item-list'),
        oTpl = document.getElementById('tpl').innerHTML,
        userId = '1111',
        userFullName = 'John',
        lng = -122.08,
        lat = 37.38,
        // lng = 40.7128,
        // lat = -74.0060,
        itemArr;

    // init
    function init() {
        // console.log('init');
        validateSession();
        bindEvent();
    }

    function validateSession() {
        switchLoginRegister('login');
    }

    function bindEvent() {
        oRegisterFormBtn.addEventListener('click', function () {
            switchLoginRegister('register');
        }, false);
        oLoginFormBtn.addEventListener('click', function () {
            switchLoginRegister('login');
        }, false);
        //login button click event listener
        oLoginBtn.addEventListener('click', loginExecutor, false);
        //register button click event listener
        oRegisterBtn.addEventListener('click', registerExecutor, false);

        oNearbyBtn.addEventListener('click', loadNearbyData, false);
        oFavBtn.addEventListener('click', loadFavoriteItems, false);
        oRecommendBtn.addEventListener('click', loadRecommendedItems, false);
        oItemList.addEventListener('click', changeFavoriteItem, false);
    }

    function loadFavoriteItems() {
        activateBtn('fav-btn');
        var opt = {
            method: 'GET',
            url: './history?user_id=' + userId,
            data: null,
            message: 'favorite'
        }
        serverExecutor(opt);
    }

    function loadRecommendedItems() {
        activateBtn('recommend-btn');
        var opt = {
            method: 'GET',
            url: './recommendation?user_id=' + userId + '&lat=' + lat + '&lon=' + lng,
            data: null,
            message: 'recommended'
        }
        serverExecutor(opt);
    }

    function changeFavoriteItem(evt) {
        var tar = evt.target,
            oParent = tar.parentElement;

        if (oParent && oParent.className === 'fav-link') {
            console.log('change ...');
            var oCurLi = oParent.parentElement,
                classname = tar.className,
                isFavorite = classname === 'fa fa-heart',
                oItems = oItemList.getElementsByClassName('item'),
                index = Array.prototype.indexOf.call(oItems, oCurLi),
                url = './history',
                req = {
                    user_id: userId,
                    favorite: itemArr[index]
                },
                method = !isFavorite ? 'POST' : 'DELETE';

                ajax({
                    method: method,
                    url: url,
                    data: req,
                    success: function (res) {
                        if (res.status === 'OK' || res.result === 'SUCCESS') {
                            tar.className = !isFavorite ? 'fa fa-heart' : 'fa fa-heart-o';
                        } else {
                            throw new Error('Change Favorite failed!');
                        }
                    },
                    error: function () {
                        throw new Error('Change Favorite failed!')
                    }
                })
        }
    }

    function loginExecutor() {
        // console.log('login');
        var username = oLoginUsername.value,
            password = oLoginPwd.value;

        if (username === '' || password === '') {
            oLoginErrorField.innerHTML = 'Please fill in all fields';
            return;
        }

        password = md5(username + md5(password));
        // console.log(username, password);

        ajax({
            method: 'POST',
            url: './login',
            data: {
                user_id: username,
                password: password,
            },
            success: function (res) {
                if (res.status === 'OK') {
                    console.log('login');
                    console.log(res);
                    welcomeMsg(res);
                    fetchData();
                } else { //reachable?
                    oLoginErrorField.innerHTML = 'Invalid username or password';
                }
            },
            error: function () {
                throw new Error('Invalid username or password');
            }
        });
    }

    function registerExecutor() {
        // console.log('register');
        var username = oRegisterUsername.value,
            password = oRegisterPwd.value,
            firstName = oRegisterFirstName.value,
            lastName = oRegisterLastName.value;
        // console.log(username, password, firstName, lastName);
        if (username === '' || password === '' || firstName === '' || lastName === '') {
            oRegisterResultField.innerHTML = 'Please fill in all fields';
            return;
        }
        if (username.match(/^[a-z0-9_]+$/) === null) {
            oRegisterResultField.innerHTML = 'Invalid username';
            return;
        }
        password = md5(username + md5(password));
        // console.log(password);
        ajax({
            method: 'POST',
            url: './register',
            data: {
                user_id: username,
                password: password,
                first_name: firstName,
                last_name: lastName
            },
            success: function (res) {
                if (res.status === 'OK' || res.result === 'OK') {
                    oRegisterResultField.innerHTML = 'Successfully registered!';
                } else {
                    oRegisterResultField.innerHTML = 'User already exists!';
                }
            },
            error: function () {
                throw new Error('Failed to register');
            }
        });
    }

    function switchLoginRegister(name) {
        //hide header elements
        showOrHideElement(oAvatar, 'none');
        showOrHideElement(oWelcomeMsg, 'none');
        showOrHideElement(oLogoutBtn, 'none');

        //hide item list area
        showOrHideElement(oItemNav, 'none');
        showOrHideElement(oItemList, 'none');

        if (name === 'login') {
            //hide register form
            showOrHideElement(oRegisterForm, 'none');
            //clear register error
            oRegisterResultField.innerHTML = '';
            //show login form
            showOrHideElement(oLoginForm, 'block');
        } else {
            //hide login form
            showOrHideElement(oLoginForm, 'none');
            //clear login error if exists
            oLoginErrorField.innerHTML = '';
            //show register form
            showOrHideElement(oRegisterForm, 'block');
        }
    }

    function showOrHideElement(ele, style) {
        ele.style.display = style;
    }

    function ajax(opt) {
        var opt = opt || {},
            method = (opt.method || 'GET').toUpperCase(),
            url = opt.url,
            data = opt.data || null,
            success = opt.success || function () {},
            error = opt.error || function () {},
            xhr = new XMLHttpRequest();

        if (!url) {
            throw new Error('missing url');
        }

        xhr.open(method, url, true);

        xhr.onload = function () {
            if (xhr.status === 200) {
                success(JSON.parse(xhr.responseText));
            } else { //will only quietly log error if not a match?
                error();
            }
        }

        xhr.onerror = function () {
            throw new Error('the request could not be completed.');
        }

        if (!data) {
            xhr.send();
        } else {
            xhr.setRequestHeader('Content-type', 'application/json;charset=utf-8');
            xhr.send(JSON.stringify(data));
        }
    }

    function welcomeMsg(info) {
        userId = info.user_id || userId;
        userFullName = info.name || userFullName;
        oWelcomeMsg.innerHTML = 'Welcome ' + userFullName;

        //show welcome, avatar, item area, logout btn
        showOrHideElement(oWelcomeMsg, 'block');
        showOrHideElement(oAvatar, 'block');
        showOrHideElement(oItemNav, 'block');
        showOrHideElement(oItemList, 'block');
        showOrHideElement(oLogoutBtn, 'block');

        //hide login form
        showOrHideElement(oLoginForm, 'none');
    }

    function fetchData() {
        initGeo(loadNearbyData);
    }

    function loadNearbyData() {
        activateBtn('nearby-btn');

        var opt = {
            method: 'GET',
            url: './search?user_id=' + userId + '&lat=' + lat + '&lon=' + lng,
            data: null,
            message: 'nearby'
        }
        serverExecutor(opt);
    }

    function serverExecutor(opt) {
        oItemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i>Loading ' + opt.message + ' item...</p>';
        ajax({
            method: opt.method,
            url: opt.url,
            data: opt.data,
            success: function (res) {
                if (!res || res.length === 0) {
                    oItemList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i>No ' + opt.message + ' item!</p>';
                } else {
                    render(res);
                    itemArr = res;
                }
            },
            error: function () {
                throw new Error('No ' + opt.message + ' items!');
            }
        })
    }

    function initGeo(cb) {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                // lat = position.coords.latitude || lat;
                // lng = position.coords.longitude || lng;
                cb();
            },
            function () {
                throw new Error('Geo location fetch failed!!');
            },
                {maximumAge: 60000}
            );
            oItemList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i>Retrieving your location...</p>';
        } else {
            throw new Error('Your browser does not support navigator!!');
        }
    }

    function activateBtn(btnId) {
        var len = oNavBtnList.length;
        for (var i = 0; i < len; i++) {
            oNavBtnList[i].className = 'main-nav-btn';
        }
        var btn = document.getElementById(btnId);
        btn.className += ' active';
    }

    function render(data) {
        var len = data.length,
            list = '',
            item;
        for (var i = 0; i < len; i++) {
            item = data[i];
            list += oTpl.replace(/{{(.*?)}}/gmi, function (node, key) {
                console.log(key);
                if (key === 'company_logo') {
                    return item[key] || 'https://via.placeholder.com/100';
                }
                if (key === 'location') {
                    return item[key].replace(/,/g, '<br/>').replace(/\"/g,'');
                }
                if (key === 'favorite') {
                    return item[key] ? "fa fa-heart" : "fa fa-heart-o";
                }
                return item[key];
            });
        }
        oItemList.innerHTML = list;
    }

    init();
})();
