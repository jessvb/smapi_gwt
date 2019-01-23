<?php
  header('Content-Type: text/javascript');
  header('Cache-Control: no-cache');
  header('Pragma: no-cache');
  
  $callback = trim($_GET['callback']);
  $accessToken = trim($_GET['accessToken']);
  $dir = trim($_GET['dir']); // e.g., dir='/v1/skills/{skillid}'
  $q = trim($_GET['q']); // e.g., q='?vendorId={vendorId}'

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
   $smapiUrl = "https://api.amazonalexa.com" . $dir;
   if (!empty($q)) {
      $smapiUrl .= "?" . $q;
   }
   $auth = "Authorization: Bearer ".$accessToken;
   curl_setopt($ch, CURLOPT_URL, $smapiUrl);
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
