
function check(string) {
    l_num = 0;
    var sz = false, dzm = false, xzm = false, ts = false;
    for (i = 0; i < string.length; i++) {
        asc = checkstr(string.charCodeAt(i));
        if (asc == 1 && sz == false) {
            l_num += 1;
            sz = true;
        }
        if (asc == 3 && xzm == false) {
            l_num += 2;
            xzm = true;
        }
        if (asc == 2 && dzm == false) {
            l_num += 2;
            dzm = true;
        }
        if (asc == 4 && ts == false) {
            l_num += 10;
            ts = true;
        }
    }
    return l_num;
}

function checkstr(str) {
    if (str >= 48 && str <= 57) {//数字
        return 1;
    } else if (str >= 65 && str <= 90) {//大写字母
        return 2;
    } else if (str >= 97 && str <= 122) {//小写字母
        return 3;
    } else {//特殊字符
        return 4;
    }
}

function containSpace(str){
    var reg = /(\s+)/g;
    return reg.test(str);
}
//去掉空格
function trimAll(str){
    return str.replace(/(^\s*)|(\s*)|(\s*$)/g, "");
}