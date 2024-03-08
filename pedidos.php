<?php
include "config.php";
include "utils.php";
$dbConn = connect($db);
/*
  Listar todos los pedidos pendientes
 */
if ($_SERVER['REQUEST_METHOD'] == 'GET')
{
    if (isset($_GET['idPedido']))
    {
      // Mostrar detalles de un pedido
      $sql = $dbConn->prepare("SELECT * FROM pedidos WHERE idPedido=:idPedido");
      $sql->bindValue(':idPedido', $_GET['idPedido']);
      $sql->execute();
      $sql->setFetchMode(PDO::FETCH_ASSOC);
      header("HTTP/1.1 200 OK");
      echo json_encode($sql->fetchAll());
      exit();
    }
    else
    {
       // Todos los pedidos pendientes
       $sql = $dbConn->prepare("SELECT * FROM pedidos ORDER BY fechaPedido");
       $sql->execute();
       $sql->setFetchMode(PDO::FETCH_ASSOC);
       header("HTTP/1.1 200 OK");
       echo json_encode($sql->fetchAll());
       exit();
    }
}
// Crear un nuevo pedido
if ($_SERVER['REQUEST_METHOD'] == 'POST')
{
    $input = $_POST;
    $sql = "INSERT INTO pedidos VALUES (null, CURDATE(), '".$_POST['fechaEstimadaPedido']."', '".$_POST['descripcionPedido']."', ".$_POST['importePedido'].",0, ".$_POST['idTiendaFK'].")";
    $statement = $dbConn->prepare($sql);
    $statement->execute();
    $postId = $dbConn->lastInsertId();
    if($postId)
    {
      $input['idApunte'] = $postId;
      header("HTTP/1.1 200 OK");
      echo json_encode($input);
      exit();
   }
}
// Borrar
if ($_SERVER['REQUEST_METHOD'] == 'DELETE')
{
  $idPedido = $_GET['idPedido'];
  $statement = $dbConn->prepare("DELETE FROM pedidos WHERE idPedido=:idPedido");
  $statement->bindValue(':idPedido', $idPedido);
  $statement->execute();
  header("HTTP/1.1 200 OK");
  exit();
}
// Actualizar
if ($_SERVER['REQUEST_METHOD'] == 'PUT')
{
    $input = $_GET;
	  $sql = "UPDATE pedidos SET fechaPedido='".$input['fechaPedido']."',fechaEstimadaPedido='".$input['fechaEstimadaPedido']."',descripcionPedido='".$input['descripcionPedido']."',importePedido=".$input['importePedido'].",estadoPedido=".$input['estadoPedido'].",idTiendaFK=".$input['idTiendaFK']." WHERE idPedido=".$input['idPedido'];
    $statement = $dbConn->prepare($sql);
    $statement->execute();
    header("HTTP/1.1 200 OK");
    exit();
}
// En caso de que ninguna de las opciones anteriores se haya ejecutado
header("HTTP/1.1 400 Bad Request");
?>
