import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiServiceService } from '../../services/api-service.service';
import { UsuarioModelModule } from '../../models/usuario-model/usuario-model.module';
import { PuntosModelModule } from '../../models/puntos-model/puntos-model.module';
import Swal from 'sweetalert2';
import { RutasModelModule } from '../../models/rutas-model/rutas-model.module';

@Component({
  selector: 'app-admin-tiros',
  templateUrl: './admin-tiros.component.html',
  styleUrls: ['./admin-tiros.component.scss']
})
export class AdminTirosComponent implements OnInit {

  datosVendedor: any;
  itemVendedor: any;

  datosPunto: any;
  itemPunto: any;

  datosRutas: any;
  itemRutas: any;

  datosRes: any;
  itemRes: any;

  formData: FormData = new FormData();
  rutasModel: RutasModelModule = new RutasModelModule();
  puntoModel: PuntosModelModule = new PuntosModelModule();
  vendedorModel: UsuarioModelModule = new UsuarioModelModule();

  constructor(
    private apiService: ApiServiceService,
    private router: Router,
    private route: ActivatedRoute
  ) { }

  id: any = this.route.snapshot.paramMap.get('id');

  ngOnInit(): void {
    this.datosTiros();
  }

  datosTiros(): void {
    if (this.id === 'add') {
      this.apiService.listaUsuarios().subscribe(
        res => {
          this.datosVendedor = res;
          this.itemVendedor = this.datosVendedor['Usuarios'];
        }
      );
    }
  }

  vendedorSeleccionado(): void {
    this.formData.append('id', this.vendedorModel.idTipo.toString());
    this.apiService.listaRutaUsuario(this.formData).subscribe(
      res => {
        this.datosRutas = res;
        this.itemRutas = this.datosRutas['Rutas'];
        console.log(this.itemRutas);
      }
    );
  }

  rutaSeleccionada(): void {
    this.formData.append('idRuta', this.rutasModel.idVendedor.toString());
    this.apiService.listaPuntoVentaRuta(this.formData).subscribe(
      res => {
        this.datosPunto = res;
        this.itemPunto = this.datosPunto['Puntos'];
      }
    );
  }

  guardarDatos(): void {
    this.formData.append('idUsuario', this.vendedorModel.idTipo.toString());
    this.formData.append('idPVenta', this.puntoModel.id.toString());
    this.formData.append('salida', '3');
    this.apiService.generarTiro(this.formData).subscribe(
      res => {
        this.datosRes = res;
        this.itemRes = this.datosRes['Mensaje'];
        Swal.fire(
          'Creado',
          this.itemRes
        );
        this.router.navigateByUrl('/tiros');
      }
    );
  }

}
