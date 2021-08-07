import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReporteVentaComponent } from './reporte-venta.component';

describe('ReporteVentaComponent', () => {
  let component: ReporteVentaComponent;
  let fixture: ComponentFixture<ReporteVentaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReporteVentaComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReporteVentaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
