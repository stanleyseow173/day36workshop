import {HttpClient, HttpParams} from "@angular/common/http";
import {inject, Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {WeatherData} from "./models";

const URL = '/api/weather'

const CITIES_URL = '/api/cities'

const ADDCITY_URL = '/api/addCity'

@Injectable()
export class WeatherService {

  http = inject(HttpClient)

  getWeather(city: string, units = 'metric'): Observable<WeatherData[]> {
    const params = new HttpParams()
        .set("city", city)
        .set("units", units)

    return this.http.get<WeatherData[]>(`${URL}`, { params })

  }

  getCities(): Observable<string[]>{
    return this.http.get<string[]>(`${CITIES_URL}`)
  }

  addCity(city: string): Observable<string>{
    const params = new HttpParams()
        .set("city", city)

    return this.http.post<string>(`${ADDCITY_URL}`,{}, {params}) 
  }

}
