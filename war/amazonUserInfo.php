<?php
header('Content-Type: text/javascript');
header('Cache-Control: no-cache');
header('Pragma: no-cache');

$callback = trim($_GET['callback']);
$accessToken = trim($_GET['accessToken']);

$accessUrl = "https://api.amazon.com/auth/o2/tokeninfo?access_token=" . $accessToken;

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $accessUrl);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$testAuthJson = curl_exec($ch);

$testAuthJson = json_decode($testAuthJson);
// echo $testAuthJson->{'aud'};

if ($testAuthJson->{'aud'} != 'amzn1.application-oa2-client.74c0b384f93146cdb0b5d0c40bbcef72') {
    echo $callback;
    echo '({"error":"Invalid token."})';
} else {
    $userInfoUrl = "https://api.amazon.com/user/profile";
    $auth = "Authorization: Bearer ".$accessToken;
    curl_setopt($ch, CURLOPT_URL, $userInfoUrl);
    curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json' , $auth));
    $output = curl_exec($ch);

    echo $callback;
    echo '(';
    echo $output;
    echo ');';
}
// NOTE: DO **NOT** CLOSE YOUR CURL HANDLE!!! OTHERWISE THINGS WON'T WORK!
// ch_close($ch);
?>
