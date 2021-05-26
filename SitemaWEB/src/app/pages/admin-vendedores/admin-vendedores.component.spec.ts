import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminVendedoresComponent } from './admin-vendedores.component';

describe('AdminVendedoresComponent', () => {
  let component: AdminVendedoresComponent;
  let fixture: ComponentFixture<AdminVendedoresComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminVendedoresComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminVendedoresComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
