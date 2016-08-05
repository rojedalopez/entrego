<%@page import="bean.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
response.setHeader("Pragma", "No-chache"); 
response.setHeader("Expires", "0"); 
response.setHeader("Cache-Control", "no-cache"); 
response.setHeader("Cache", "no-cache"); 
if(session.getAttribute("user") != null){
   Usuario u = (Usuario)session.getAttribute("user");
   if(u.getRol()==1){
        response.sendRedirect("/admin/");
   }else if(u.getRol()==2){
        response.sendRedirect("/entrego/servicios.jsp");
   }else if(u.getRol()==3){
        response.sendRedirect("/conductor/");
   }
}
%>
<!DOCTYPE html>
<html lang="en" class="body-full-height">
    <head>        
        <!-- META SECTION -->
        <title>Joli Admin - Responsive Bootstrap Admin Template</title>            
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        
        <link rel="icon" href="favicon.ico" type="image/x-icon" />
        <!-- END META SECTION -->
        
        <!-- CSS INCLUDE -->        
        <link rel="stylesheet" type="text/css" id="theme" href="css/theme-default.css"/>
        <!-- EOF CSS INCLUDE -->                                    
        
        <script type="text/javascript" src="js/plugins/jquery/jquery.min.js"></script>
        <script type="text/javascript" src="js/plugins/jquery/jquery-ui.min.js"></script>
        <script type="text/javascript" src="js/plugins/bootstrap/bootstrap.min.js"></script>  
        <!-- THIS PAGE PLUGINS -->
        <script type='text/javascript' src='js/plugins/icheck/icheck.min.js'></script>
        <script type="text/javascript" src="js/plugins/mcustomscrollbar/jquery.mCustomScrollbar.min.js"></script>
        
        <script type="text/javascript" src="js/plugins/bootstrap/bootstrap-datepicker.js"></script>                
        <script type="text/javascript" src="js/plugins/bootstrap/bootstrap-file-input.js"></script>
        <script type="text/javascript" src="js/plugins/bootstrap/bootstrap-select.js"></script>
        <script type="text/javascript" src="js/plugins/tagsinput/jquery.tagsinput.min.js"></script>
        <!-- END THIS PAGE PLUGINS -->       
        
        <!-- START TEMPLATE -->
        <script type="text/javascript" src="js/settings.js"></script>
        
        <script type="text/javascript" src="js/plugins.js"></script>        
        <script type="text/javascript" src="js/actions.js"></script> 
    </head>
    <body>
        
        <div class="login-container">
        
            <div class="login-box animated fadeInDown">
                <div style="color: #fff; font-size: 30px; text-align: center; margin-bottom: 10px;">EntreGO</div>
                <div class="login-body">
                    <div class="login-title"><strong>Bienvenido</strong>, inicia sesion</div>
                    <form action="login_usuario" class="form-horizontal" method="post">
                    <div class="form-group">
                        <div class="col-md-12">
                            <input type="text" name="nick" class="form-control" placeholder="E-mail"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-12">
                            <input type="password" name="pass" class="form-control" placeholder="Contraseña"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-md-6">
                            <a href="#" class="btn btn-link btn-block" data-toggle="modal" data-target="#ForgotPass">Olvidaste tu contraseña?</a>
                        </div>
                        <div class="col-md-6">
                            <button class="btn btn-info btn-block">Inicia sesion</button>
                        </div>
                    </div>
                    </form>
                </div>
                <div class="login-footer">
                    <div class="pull-right">
                        No eres usuario aun? <a href="#" data-toggle="modal" data-target="#NewUser">Registrate aqui</a>
                    </div>
                </div>
            </div>
            
        </div>
        

        <!-- Modal -->
        <div class="modal fade" id="ForgotPass" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
              </div>
              <div class="modal-body">
                ...
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary">Save changes</button>
              </div>
            </div>
          </div>
        </div>
        
        <div class="modal fade" id="NewUser" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">Nuevo usuario</h4>
              </div>
              <div class="modal-body">
                  <form class="form-horizontal">
                    <div class="form-group">
                        <label class="col-md-3 col-xs-12 control-label">Tipo de usuario</label>
                        <div class="col-md-8 col-xs-12">                                                                                            
                            <select class="form-control select">
                                <option>Persona natural</option>
                                <option>Persona juridica</option>
                            </select>
                        </div>
                    </div> 
                    <div class="form-group">
                        <label class="col-md-3 col-xs-12 control-label">Nombres</label>
                        <div class="col-md-8 col-xs-12">                                            
                            <div class="input-group">
                                <span class="input-group-addon"><span class="fa fa-pencil"></span></span>
                                <input type="text" class="form-control"/>
                            </div>                                            
                        </div>
                    </div>
                      
                    <div class="form-group">
                        <label class="col-md-3 col-xs-12 control-label">Apellidos</label>
                        <div class="col-md-8 col-xs-12">                                            
                            <div class="input-group">
                                <span class="input-group-addon"><span class="fa fa-pencil"></span></span>
                                <input type="text" class="form-control"/>
                            </div>                                            
                        </div>
                    </div>
                      
                    <div class="form-group">
                        <label class="col-md-3 col-xs-12 control-label">Tipo de documento</label>
                        <div class="col-md-8 col-xs-12">                                                                                            
                            <select class="form-control select">
                                <option>Cedula de ciudadania</option>
                                <option>Tarjeta de identidad</option>
                            </select>
                        </div>
                    </div>
                      
                    <div class="form-group">
                        <label class="col-md-3 col-xs-12 control-label">Numero del documento</label>
                        <div class="col-md-8 col-xs-12">                                            
                            <div class="input-group">
                                <span class="input-group-addon"><span class="fa fa-pencil"></span></span>
                                <input type="text" class="form-control"/>
                            </div>                                            
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 col-xs-12 control-label">Correo electronico</label>
                        <div class="col-md-8 col-xs-12">                                            
                            <div class="input-group">
                                <span class="input-group-addon"><span class="fa fa-pencil"></span></span>
                                <input type="text" class="form-control"/>
                            </div>                                            
                        </div>
                    </div>
                      
                    <div class="form-group">                                        
                        <label class="col-md-3 col-xs-12 control-label">Contraseña</label>
                        <div class="col-md-8 col-xs-12">
                            <div class="input-group">
                                <span class="input-group-addon"><span class="fa fa-unlock-alt"></span></span>
                                <input type="password" class="form-control"/>
                            </div>            
                            <span class="help-block">*Minimo 6 caracteres</span>
                        </div>
                    </div>

                    <div class="form-group">                                        
                        <label class="col-md-3 col-xs-12 control-label">Repita contraseña</label>
                        <div class="col-md-8 col-xs-12">
                            <div class="input-group">
                                <span class="input-group-addon"><span class="fa fa-unlock-alt"></span></span>
                                <input type="password" class="form-control"/>
                            </div>            
                        </div>
                    </div>

                    
                </form>
                  
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary">Crear usuario</button>
              </div>
            </div>
          </div>
        </div>
        
    </body>
</html>






