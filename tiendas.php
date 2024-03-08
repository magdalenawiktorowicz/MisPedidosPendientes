<?php
include "config.php";
include "utils.php";
$dbConn = connect($db);
/*
  Listar todos los registros o solo uno
 */
if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['idTienda']))
    {
      // Mostrar una tienda
      $sql = $dbConn->prepare("SELECT * FROM tiendas WHERE idTienda=:idTienda");
      $sql->bindValue(':idTienda', $_GET['idTienda']);
      $sql->execute();
      header("HTTP/1.1 200 OK");
      echo json_encode($sql->fetch(PDO::FETCH_ASSOC));
      exit();
    }
    else 
	{
      // Mostrar lista de tiendas
      $sql = $dbConn->prepare("SELECT * FROM tiendas");
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode($sql->fetchAll());
      exit();
  }
}
// Crear una nueva tienda
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    $input = $_POST;
    $sql = "INSERT INTO tiendas VALUES (null,'".$_POST['nombreTienda']."')";
    $statement = $dbConn->prepare($sql);
    $statement->execute();
    $postId = $dbConn->lastInsertId();
    if($postId)
    {
      $input['idTienda'] = $postId;
      header("HTTP/1.1 200 OK");
      echo json_encode($input);
      exit();
   }
}
// Borrar
if ($_SERVER['REQUEST_METHOD'] == 'DELETE')
{
  $idTienda = $_GET['idTienda'];
  $statement = $dbConn->prepare("DELETE FROM tiendas WHERE idTienda=:idTienda");
  $statement->bindValue(':idTienda', $idTienda);
  $statement->execute();
  header("HTTP/1.1 200 OK");
  exit();
}
// Actualizar
if ($_SERVER['REQUEST_METHOD'] == 'PUT')
{
    $input = $_GET;
	  $sql = "UPDATE tiendas SET nombreTienda='".$input['nombreTienda']."' WHERE idTienda=".$input['idTienda'];
    $statement = $dbConn->prepare($sql);
    $statement->execute();
    header("HTTP/1.1 200 OK");
    exit();
}
// En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");
?>
