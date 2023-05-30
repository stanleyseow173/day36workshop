import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Params, Router} from '@angular/router';
import { firstValueFrom, Observable, Subject } from 'rxjs';
import {CityQuery, WeatherQuery} from '../models';
import { WeatherService } from '../weather.service';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit{

  fb = inject(FormBuilder)
  router = inject(Router)

  form!: FormGroup

  formCity!: FormGroup

  cities$!: Observable<string[]>

  canAdd!: boolean

  @ViewChild('inputCity')
  inputCity!: ElementRef

  cityValue$ = new Subject<string>()

  cityValid!: AbstractControl

  constructor(private weatherSvc: WeatherService){

    
  }

  ngOnInit(): void {
    this.form = this.createForm();
    this.cityValid = this.form.controls['city']
    this.cities$ = this.weatherSvc.getCities()
    this.formCity = this.fb.group({city: this.fb.control<string>('',[Validators.required])})
  }

  addCity(){
    const cityQuery = this.formCity.value as CityQuery
    console.info('>>> cityQuery: ', cityQuery)
    //console.info('>>>: ',  this.formCity.value)
    console.info("firstvalue from : ", firstValueFrom(this.weatherSvc.addCity(cityQuery.city)))
    setTimeout(()=>{this.router.navigate(['/'])},5000)
    this.cities$ = this.weatherSvc.getCities()
  }

  delay(ms: number){
    return new Promise(resolve => setTimeout(resolve,ms)); 
  }

  process() {
    const query = this.form.value as WeatherQuery
    console.info('>>> query: ', query)
    const queryParams: Params =  { units: query.units }
    this.router.navigate([ '/weather', query.city ], { queryParams: queryParams })
  }

  processClick(event: any){
    console.info("event is >>>", event.srcElement.id)
    this.cityValue$.next(event.srcElement.id)
    const cityTitleCase = event.srcElement.id.split(" ").map((l: string) => l[0].toUpperCase() + l.substring(1)).join(" ")
    this.inputCity.nativeElement.value = cityTitleCase
    console.info("native element >>>",  this.inputCity.nativeElement.value)
    const query = this.form.value as WeatherQuery
    const unitsExtracted = query.units
    this.form.setValue({city: cityTitleCase, units: unitsExtracted})
    this.cityValid.updateValueAndValidity()
  }

  private createForm() {
    return this.fb.group({
      city: this.fb.control<string>('', [ Validators.required, Validators.minLength(2)]),
      units: this.fb.control('metric', [ Validators.required ])
    })
  }

}
