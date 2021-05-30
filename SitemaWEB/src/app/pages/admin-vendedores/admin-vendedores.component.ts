import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ApiServiceService } from '../../services/api-service.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-admin-vendedores',
  templateUrl: './admin-vendedores.component.html',
  styleUrls: ['./admin-vendedores.component.scss']
})
export class AdminVendedoresComponent implements OnInit {

  constructor(
    private apiService: ApiServiceService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  id: any = this.route.snapshot.paramMap.get('id');

  ngOnInit(): void {

    if (localStorage.getItem('Usuario') === null) {
      this.router.navigateByUrl('/login');
    }
  }

}
