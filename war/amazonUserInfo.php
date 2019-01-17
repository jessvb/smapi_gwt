<?php
  header('Content-Type: text/javascript');
  header('Cache-Control: no-cache');
  header('Pragma: no-cache');
  
  $callback = trim($_GET['callback']);
  echo $callback;

  echo '(';
  echo '{
    "access_token":"Atza|IQEBLjAsAhRmHjNgHpi0U-Dme37rR6CuUpSR...",
    "token_type":"bearer",
    "expires_in":3600,
    "refresh_token":"Atzr|IQEBLzAtAhRPpMJxdwVz2Nn6f2y-tpJX2DeX..."
  }';
  echo ')';
?>
