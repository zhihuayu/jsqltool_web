<?php

function httpPost($url,$requestString, $timeout = 6000)
{
    if ($url == '' || $requestString == '' || $timeout <= 0) {
        return false;
    }
    // 1.初始化
    $ch = curl_init();
    // 2.设置参数
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_HEADER, 0); // CURLLOPT_HEADER设置为0表示不返回HTTP头部信息。
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); // 设置为1表示稍后执行的curl_exec函数的返回是URL的返回字符串，而不是把返回字符串定向到标准输出并返回TRUE；
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_TIMEOUT, (int) $timeout);
    curl_setopt($curl, CURLOPT_ENCODING, 'gzip,deflate');
    $ret = curl_exec($ch);
}
