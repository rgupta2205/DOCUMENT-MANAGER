<?php header

  ('Access-Control-Allow-Origin: *'); 
   $con=mysqli_connect('localhost','root','','map_innovative_user');
    if($con==false)
    {
      echo("Connection is failed");
    }


    $email=$_GET['email'];
    $pass=$_GET['pass'];
  
    $sql="SELECT * FROM `user` WHERE Email='$email' AND Password='$pass'";
    $run=mysqli_query($con,$sql)or die("Na ho paya");
      
    $line=mysqli_num_rows($run);
    $res=array();
    if($line==1)
    {
      $res['count']=1;
    }
    else
    {
      $res['count']=0;
    }
      
    echo json_encode($res);
    
?>

