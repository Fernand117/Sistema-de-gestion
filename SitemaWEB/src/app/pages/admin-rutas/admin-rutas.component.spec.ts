import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRutasComponent } from './admin-rutas.component';

describe('AdminRutasComponent', () => {
  let component: AdminRutasComponent;
  let fixture: ComponentFixture<AdminRutasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminRutasComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminRutasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
