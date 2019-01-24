<?php
  header('Content-Type: text/javascript');
  header('Cache-Control: no-cache');
  header('Pragma: no-cache');
  
  $callback = trim($_GET['callback']);
  $accessToken = trim($_GET['accessToken']);
  $dir = trim($_GET['dir']); // e.g., dir='/v1/skills/{skillid}'
  $json_str = trim($_GET['json']); // e.g., json="{this:"is technically just a string"}"
  $vendorId = trim($_GET['vendorId']);

  // Update the vendorId
  $json = json_decode($json_str);
  $json->vendorId = $vendorId;
  $json_str = json_encode($json);

 // echo $json_str;
  // Ensure validity of access token
  $accessUrl = "https://api.amazon.com/auth/o2/tokeninfo?access_token=" . $accessToken;

 $ch = curl_init();
 curl_setopt($ch, CURLOPT_URL, $accessUrl);
 curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
 $testAuthJson = curl_exec($ch);

 $testAuthJson = json_decode($testAuthJson);

 // Test validity of access token here:
 if ($testAuthJson->{'aud'} != 'amzn1.application-oa2-client.74c0b384f93146cdb0b5d0c40bbcef72') {
  echo $callback;
  echo '({"error":"Invalid token."})';
 } else {
   $smapiUrl = "https://api.amazonalexa.com" . $dir;
   $auth = "Authorization: Bearer ".$accessToken;

   // Set the options for POST
   curl_setopt($ch, CURLOPT_URL, $smapiUrl);
   curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
   curl_setopt($ch, CURLOPT_HTTPHEADER, array(
	   'Content-Type: application/json' , 
           $auth, 
           'Content-Length: ' . strlen($json_str))
   );
   curl_setopt($ch, CURLOPT_POSTFIELDS, $json_str);
 
   // Send request and put the output into $output
   $output = curl_exec($ch);
  
   echo $callback;
   echo '(';
   echo $output;
   echo ');';
 }
 // echo $smapiUrl;
//// NOTE: DO **NOT** CLOSE YOUR CURL HANDLE!!! OTHERWISE THINGS WON'T WORK!
?>
