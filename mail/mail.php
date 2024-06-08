<?php
   use PHPMailer\PHPMailer\PHPMailer; 
   use PHPMailer\PHPMailer\Exception; 
  
   require 'vendor/autoload.php'; 
  
   $mail = new PHPMailer(true); 

   $con=mysqli_connect('localhost','root','','map_innovative_user');
    if($con==false)
    {
      echo("Connection is failed");
    }

   if(isset($_GET['change_password']))
   {
     try { 
        
         $rand=rand(100000,999999);     
         $to=$_GET['change_password'];  

         $sql="UPDATE `user` SET Password='$rand' WHERE Email='$to'";
         $run=mysqli_query($con,$sql)or die("Na ho paya");

         $mail->isSMTP();                                             
         $mail->Host       = 'smtp.gmail.com;';                     
         $mail->SMTPAuth   = true;                              
         $mail->Username   = 'meet0fatel@gmail.com';                  
         $mail->Password   = 'meet14042000';                         
         $mail->SMTPSecure = 'tls';                               
         $mail->Port       = 587;   
       
         $mail->setFrom('from@gfg.com', 'Meet');            
         $mail->addAddress($to); 
        
         $mail->isHTML(true);                                   
         $mail->Subject = 'Password Change'; 
         $mail->Body    = 'your one time password is <b>'.$rand.'</b> '; 
         $mail->AltBody = 'Body in plain text for non-HTML mail clients'; 
         $mail->send(); 

        echo "Mail has been sent successfully!"; 
     } 
     catch (Exception $e) { 
        echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}"; 
     } 
   }
   else if(isset($_GET['add_me']))
   {
      $name=$_GET['add_me'];
      $pass=$_GET['pass'];
      $email=$_GET['email'];

      $sql="INSERT INTO `user`(`ID`, `Name`, `Email`, `Password`) VALUES (null,'$name','$email','$pass')";
      $run=mysqli_query($con,$sql)or die("Na ho paya");

      $res=array();
      $res['status']="done";
      echo json_encode($res);

   }
   else if(isset($_GET['update_pass']))
   {
      $name=$_GET['update_pass'];
      $pass=$_GET['pass'];

      $sql="UPDATE `user` SET Password='$pass' WHERE Email='$name'";
      $run=mysqli_query($con,$sql)or die("Na ho paya");



      $res=array();
      $res['kamkaj']="done";
      echo json_encode($res);

   }
?>