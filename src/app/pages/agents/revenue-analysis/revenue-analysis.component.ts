import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration, ChartType, ChartData } from 'chart.js';

@Component({
  selector: 'app-revenue-analysis',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './revenue-analysis.component.html',
  styleUrls: ['./revenue-analysis.component.css']
})
export class RevenueAnalysisComponent implements OnInit {
  token: string = localStorage.getItem('token') || '';
  apiUrl: string = 'http://localhost:8080/bookings/revenue';

  chartDataMap: { [key: string]: ChartData<ChartType> } = {};
  chartTypeMap: { [key: string]: ChartType } = {
    stylists: 'bar',
    services: 'bar',
    shops: 'doughnut',
    days: 'line',
    months: 'bar'
  };
  chartOptionsMap: { [key: string]: ChartConfiguration['options'] } = {};

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    ['stylists', 'services', 'shops', 'days', 'months'].forEach((type) => {
      this.loadChart(type);
    });
  }

  loadChart(type: string) {
   
    const fakeData: { [key: string]: { labels: string[], data: number[] } } = {
      stylists: {
        labels: ['John', 'Anna', 'Mike', 'Sara', 'Tom'],
        data: [2500000, 3000000, 1500000, 2800000, 3200000]
      },
      services: {
        labels: ['Haircut', 'Shampoo', 'Dye', 'Massage', 'Combo'],
        data: [2200000, 1800000, 1400000, 1200000, 3000000]
      },
      shops: {
        labels: ['Shop A', 'Shop B', 'Shop C'],
        data: [5000000, 7000000, 4500000]
      },
      days: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        data: [1000000, 1200000, 900000, 1300000, 1500000, 1800000, 2000000]
      },
      months: {
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
        data: [5000000, 5500000, 6000000, 7000000, 7500000, 8000000]
      }
    };
  
    const res = fakeData[type];
  
    if (!res) return;
  
    if (type === 'days') {
      this.chartDataMap[type] = {
        labels: res.labels,
        datasets: [{
          label: 'Daily Revenue',
          data: res.data,
          borderColor: '#f97316',
          backgroundColor: 'rgba(249,115,22,0.1)',
          fill: true,
          tension: 0.4,
          pointBackgroundColor: '#f97316',
          pointRadius: 4,
          borderWidth: 2
        }]
      };
    } else if (type === 'months') {
      this.chartDataMap[type] = {
        labels: res.labels,
        datasets: [
          {
            type: 'bar',
            label: 'Monthly Revenue',
            data: res.data,
            backgroundColor: '#ff8fa3',
            borderRadius: 6
          },
          {
            type: 'line',
            label: 'Trend',
            data: res.data,
            borderColor: '#3b82f6',
            backgroundColor: 'rgba(59,130,246,0.3)',
            fill: true,
            tension: 0.4
          }
        ]
      };
    } else if (type === 'shops') {
      this.chartDataMap[type] = {
        labels: res.labels,
        datasets: [{
          label: 'Shops',
          data: res.data,
          backgroundColor: this.generateColors(res.labels.length)
        }]
      };
    } else {
      this.chartDataMap[type] = {
        labels: res.labels,
        datasets: [{
          label: `Revenue by ${this.getTitle(type)}`,
          data: res.data,
          backgroundColor: '#f97316',
          borderColor: '#f59e0b',
          borderWidth: 1
        }]
      };
    }
  
    this.chartOptionsMap[type] = {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: { labels: { color: '#ccc' }, position: 'top' },
        title: { display: true, text: `${this.getTitle(type)} Analysis`, color: '#fff' }
      },
      scales: {
        x: { ticks: { color: '#ccc' }, grid: { color: '#333' } },
        y: { ticks: { color: '#ccc' }, grid: { color: '#333' }, beginAtZero: true }
      }
    };
  }
  
  generateColors(length: number): string[] {
    const palette = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899', '#22d3ee', '#f97316'];
    return Array.from({ length }, (_, i) => palette[i % palette.length]);
  }

  getTitle(type: string): string {
    switch (type) {
      case 'stylists': return 'Stylist';
      case 'services': return 'Service';
      case 'shops': return 'Shop';
      case 'days': return 'Day';
      case 'months': return 'Month';
      default: return '';
    }
  }
}
