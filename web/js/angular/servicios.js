'use strict';

angular.module('MyApp.Servicios', []).controller('ServiciosController', ['$http', '$templateCache', '$timeout', '$alert' , '$modal', 
    function($http, $templateCache, $timeout, $alert, $modal) {
    var vm = this;
    vm.busqueda={porpage:20, pageno:1, q:""};
    vm.servicios = [];
    vm.servicio={cod_servicio:"", id_estado:"", desc_estado:"", id_dir_origen:"", dir_origen:"", 
    id_dir_destino:"", dir_destino:"", nota:"", creacion:"", cod_usuario:"", nombre_usuario:"",
    apellido_usuario:"", celular_usuario:"", cod_mensajero:"", nombre_mensajero:"",
    apellido_mensajero:"", celular_mensajero:""};
    vm.pageno = 1; // initialize page no to 1
    vm.total_count = 0;
    vm.itemsPerPage = 20; //this could be a dynamic value from a drop down

    vm.getData = function(pageno){ // This would fetch the data on page change.
        console.log("entro");
        //In practice this should be in a factory.
        vm.busqueda.porpage = vm.itemsPerPage;
        vm.busqueda.pageno = pageno;
        vm.servicios = []; 

        $http.post("list_servicios", vm.busqueda).success(function(response){ 
            //ajax request to fetch data into vm.data
            vm.servicios = response.data;  // data to be displayed on current page.
            console.log(vm.servicios);
            vm.total_count = response.total_count; // total data count.
            //Modal_filter.modal('hide');
        });
    };
    
    vm.getData(vm.pageno);
    
    /*
    function MyModalController($scope) {
        $scope.title = 'Detalle de servicio';
        $scope.servicio = vm.servicio;
        $scope.vehiculos = vm.servicio.vehiculos;
         
        $scope.abrirFotos =  function(id){
            console.log(id);
            for(var i = 0; i < $scope.vehiculos.length; i++){
                if($scope.vehiculos[i].servicio === id) {
                    console.log(id);
                   $scope.vehiculo = angular.copy($scope.vehiculos[i]);
                   $scope.fotos = $scope.vehiculo.fotos;
                   vm.fotos = $scope.vehiculo.fotos;
                   console.log(vm.fotos);
                   vm.showModalFotos();
                   break;
                }
            }
        };
    }
    
    function FotosController($scope) {
        $scope.title = 'Fotos del servicio';
        var slides = $scope.slides = [];
        var currIndex = 0;
        for (var i = 0; i < vm.fotos.length; i++) {
            slides.push({
                image: '../upload/'+vm.fotos[i].url,
                text: vm.fotos[i].desc,
                id: currIndex++
            });
            console.log(vm.fotos[i]);
        }
    }
    
     MyModalController.$inject = ['$scope'];
    var myAlert = $alert({title: 'Holy guacamole!', content: 'Best check yo self, you\'re not looking too good.', placement: 'top', type: 'success', container:'#alerta-busqueda', show: false});
    var detSolicitud = $modal({controller: MyModalController, templateUrl: '../modal/det-solicitud.html', show: false});
    var fotos = $modal({controller: FotosController, templateUrl: '../modal/fotos.html', show: false});
    
    vm.showAlert = function() {
      myAlert.show(); // or myAlert.$promise.then(myAlert.show) if you use an external html template
    };
    
    
    vm.showModalSol = function() {
      detSolicitud.show();
    };
    
    
    vm.showModalFotos = function() {
      fotos.show();
    };
    
    vm.verDetalleSol = function(id){
        for(var i = 0; i < vm.servicios.length; i++){
            if(vm.servicios[i].id === id) {
               vm.servicio = angular.copy(vm.servicios[i]);
               console.log(vm.servicio);
               vm.showModalSol();
               break;
            }
        }
    };
    
    vm.verFotos = function(id){
        for(var i = 0; i < vm.servicios.length; i++){
            if(vm.servicios[i].id === id) {
               vm.servicio = angular.copy(vm.servicios[i]);
               console.log(vm.servicio);
               vm.showModalFotos();
               break;
            }
        }
    };
    */
    vm.formatDate = function(date){
        var dateOut = new Date(date);
        return dateOut;
    };
    
    vm.limpiar = function(){
        //vm.showModal();
        vm.dcargue=null;
        vm.ddescargue=null;
        vm.busqueda={porpage:20, pageno:1, q:"", cargue:"", descargue:"", orden:"", carga:"", estado:""};
        console.log(vm.busqueda);
        vm.getData(1);
    };
    
    vm.TipoCarga = [
        {"ID":1,"Value":"Gases"},
        {"ID":2,"Value":"Liquida"},
        {"ID":3,"Value":"Liquida Inflamable"},
        {"ID":4,"Value":"Reciduo Peligroso"},
        {"ID":5,"Value":"Refrigerada"},
        {"ID":6,"Value":"Seca"}
    ];
    
    vm.EstadoSolicitud = [
        {"ID":1, "Value": "Lanzada"},
        {"ID":2, "Value": "Asignada"},
        {"ID":3, "Value": "Enturnada para cargue"},
        {"ID":4, "Value": "Cargando"},
        {"ID":5, "Value": "Cargada"},
        {"ID":6, "Value": "En ruta"},
        {"ID":7, "Value": "En destino"},
        {"ID":8, "Value": "Enturnada para Descargue"},
        {"ID":9, "Value": "Descargando"},
        {"ID":10, "Value": "Entregada"},
        {"ID":11, "Value": "Terminada"},
        {"ID":12, "Value": "Cerrada"}
    ];
        
    vm.dtOptions = {
            bAutoWidth:true,
            stateSave: true,
            paging:false,
            bFilter: false,
            language: {
                "lengthMenu": "Mostrar _MENU_ registros",
                "zeroRecords": "No se encontraron registros",
                "info": "",
                "infoEmpty": "No se encontraron registros",
                "infoFiltered": "(filtrado de _MAX_ registros)",
                "search": "Buscar"
            }
        };
        
}]);