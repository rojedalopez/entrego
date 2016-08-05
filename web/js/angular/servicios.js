'use strict';

angular.module('MyApp.Servicios', []).controller('ServiciosController', ['$http', '$templateCache', '$timeout', '$alert' , '$modal', 'NgMap',
    function($http, $templateCache, $timeout, $alert, $modal, NgMap) {
    var vm = this;
    vm.busqueda={porpage:20, pageno:1, q:""};
    vm.servicios = [];
    vm.direcciones = [];
    vm.servicio={cod_servicio:"", id_estado:"", desc_estado:"", id_dir_origen:"", dir_origen:"", 
    id_dir_destino:"", dir_destino:"", nota:"", creacion:"", cod_usuario:"", nombre_usuario:"",
    apellido_usuario:"", celular_usuario:"", cod_mensajero:"", nombre_mensajero:"",
    apellido_mensajero:"", celular_mensajero:""};
    vm.pageno = 1; // initialize page no to 1
    vm.total_count = 0;
    vm.itemsPerPage = 20; //this could be a dynamic value from a drop down
    vm.dirAdicionales = [];

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
    
    vm.vehiculos = [{"lat":11.0041072,"lng":-74.80698129999996}];
    vm.placeChanged = function() {
      vm.place = this.getPlace();
      console.log('location', vm.place.geometry.location);
      vm.map.setCenter(vm.place.geometry.location);
      vm.vehiculos.push({lat:vm.place.geometry.location.lat, lng:vm.place.geometry.location.lng});
      console.log(vm.vehiculos.length);
    };
    
    NgMap.getMap().then(function(map) {
        vm.map = map;
    });

    vm.placeChanged = function() {
        vm.place = this.getPlace();
        vm.map.setCenter(vm.place.geometry.location);
        vm.direcciones.push({desc:this.idDireccion, lat:vm.place.geometry.location.lat(), 
            lng:vm.place.geometry.location.lng(), texto:vm.place.formatted_address});
    };
    
    vm.add = function () {
        vm.dirAdicionales.push({ 
          texto: ""
        });
    };

    vm.getData(vm.pageno);
    
    vm.buscarPedido = function(){
        vm.getData(vm.pageno);
    };
    
    vm.limpiarPedidos = function(){
        vm.busqueda={porpage:20, pageno:1, q:""};
        vm.getData(vm.pageno);
    };
    
    function MyModalController($scope) {
        $scope.title = 'Detalle de servicio';
        $scope.servicio = vm.servicio;
        
    }
    
    vm.showDetail = function(e, direccion) {
        console.log(direccion);
        vm.direccion = direccion;
        vm.map.showInfoWindow('foo-iw', direccion.texto);
    };
    
    vm.deleteDireccion = function(objeto){
        for(var i = 1; i < vm.direcciones.length; i++){
            if(vm.direcciones[i].texto === objeto.texto){
                vm.direcciones.splice(i, 1);
                break;
            }
        }
        
        for(var i = 0; i < vm.dirAdicionales.length; i++){
            if(vm.dirAdicionales[i].texto === objeto.texto){
                vm.dirAdicionales.splice(i, 1);
                break;
            }
        }
    };
    
    vm.reloadMap = function(){ 
        console.log("hp");
        google.maps.event.trigger(vm.map, 'resize');
    };
    
     function ServicioController($scope) {
        $scope.title = 'Detalle de servicio';
        $scope.servicio = vm.servicio;
        $scope.nameOrigin = null;

        $scope.direcciones = [];
        $scope.placeChanged = function(ds) {
            google.maps.event.trigger(map, 'resize');
          $scope.place = this.getPlace();
          $scope.map.setCenter($scope.place.geometry.location);
          $scope.direcciones[ds] = {descripcion:ds,lat:$scope.place.geometry.location.lat(), 
              lng:$scope.place.geometry.location.lng(), texto:$scope.place.formatted_address};
          console.log($scope.direcciones);
        };
           
         NgMap.getMap({ id: 'dialogMap' }).then(function(map) {
            $scope.dialogMap = map;
            google.maps.event.addListenerOnce($scope.dialogMap, 'idle', function(){
                 vm.mapSettings = { center: $scope.dialogMap.center, zoom: 7};
            });    

        });
        
         $scope.showDetail = function(e, direccion) {
            console.log(vehiculo);
            $scope.direccion = direccion;
            $scope.map.showInfoWindow('foo-iw', direccion.desc);
        };
        
        $scope.disableTap = function(event) {

            var input = event.target;

            // Get the predictions element
            var container = document.getElementsByClassName('pac-container');
            container = angular.element(container);

            // Apply css to ensure the container overlays the other elements, and
            // events occur on the element not behind it
            container.css('z-index', '5000');
            container.css('pointer-events', 'auto');

            // Disable ionic data tap
            container.attr('data-tap-disabled', 'true');

            // Leave the input field if a prediction is chosen
            container.on('click', function(){
                input.blur();
            });
        };
        
    }
    
       
     MyModalController.$inject = ['$scope'];
    var myAlert = $alert({title: 'Holy guacamole!', content: 'Best check yo self, you\'re not looking too good.', placement: 'top', type: 'success', container:'#alerta-busqueda', show: false});
    var detServicio = $modal({controller: MyModalController, templateUrl: 'modal/det-servicio.html', show: false});
    //var mdlservicio = $modal({controller: ServicioController, templateUrl: 'modal/servicio.html', show: true});
    
    vm.showAlert = function() {
      myAlert.show(); // or myAlert.$promise.then(myAlert.show) if you use an external html template
    };
    
    
    vm.showModalServ = function() {
      detServicio.show();
    };
    
    vm.showModalPedido = function() {
      modalServicio.modal('show');
      //mdlservicio.show();
    };
    
    vm.verDetalleServ = function(id){
        console.log(id);
        for(var i = 0; i < vm.servicios.length; i++){
            console.log(id);
            if(vm.servicios[i].cod_servicio === id) {
               vm.servicio = angular.copy(vm.servicios[i]);
               console.log(vm.servicio);
               vm.showModalServ();
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
            columnDefs: [ 
                {targets: 4,
                orderable: false},
                {targets: 5,    
                orderable: false}
            ],
            language: {
                "lengthMenu": "Mostrar _MENU_ registros",
                "zeroRecords": "No se encontraron registros",
                "info": "",
                "infoEmpty": "No se encontraron registros",
                "infoFiltered": "(filtrado de _MAX_ registros)",
                "search": "Buscar"
            }
        };
        
}]).controller('MensajerosController', ['$http', '$templateCache', '$timeout', '$alert' , '$modal', 
    function($http, $templateCache, $timeout, $alert, $modal) {
    var vm = this;
    vm.busqueda={porpage:8, pageno:1, q:""};
    vm.mensajeros = [];
    vm.mensajero={cod_mensajero:"", nombre_mensajero:"", apellido_mensajero:"", celular_mensajero:"",
    direccion_mensajero:"", img_mensajero:"", correo_mensajero:""};
    vm.pageno = 1; // initialize page no to 1
    vm.total_count = 0;
    vm.itemsPerPage = 8; //this could be a dynamic value from a drop down

    vm.getData = function(pageno){ // This would fetch the data on page change.
        console.log("entro");
        //In practice this should be in a factory.
        vm.busqueda.porpage = vm.itemsPerPage;
        vm.busqueda.pageno = pageno;
        vm.mensajeros = []; 

        $http.post("list_mensajeros", vm.busqueda).success(function(response){ 
            //ajax request to fetch data into vm.data
            vm.mensajeros = response.data;  // data to be displayed on current page.
            console.log(vm.mensajeros);
            vm.total_count = response.total_count; // total data count.
            //Modal_filter.modal('hide');
        });
    };
    
    vm.getData(vm.pageno);
    
    vm.buscarMensajero = function(){
        vm.getData(vm.pageno);
    };
    
    vm.limpiarMensajeros = function(){
        vm.busqueda={porpage:8, pageno:1, q:""};
        vm.getData(vm.pageno);
    };
    
    function MyModalController($scope) {
        $scope.title = 'Detalle de servicio';
        $scope.mensajero = vm.mensajero;
        
    }
    
       
     MyModalController.$inject = ['$scope'];
    var myAlert = $alert({title: 'Holy guacamole!', content: 'Best check yo self, you\'re not looking too good.', placement: 'top', type: 'success', container:'#alerta-busqueda', show: false});
    var detMensajero = $modal({controller: MyModalController, templateUrl: 'modal/det-servicio.html', show: false});
    
    
    
    vm.showAlert = function() {
      myAlert.show(); // or myAlert.$promise.then(myAlert.show) if you use an external html template
    };
    
    
    vm.showModalMens = function() {
      detMensajero.show();
    };
    

    vm.verDetalleServ = function(id){
        console.log(id);
        for(var i = 0; i < vm.mensajeros.length; i++){
            console.log(id);
            if(vm.mensajeros[i].cod_mensajero === id) {
               vm.mensajero = angular.copy(vm.mensajeros[i]);
               console.log(vm.mensajero);
               vm.showModalMens();
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