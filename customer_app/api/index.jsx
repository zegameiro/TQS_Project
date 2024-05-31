import axios from 'axios';

export default axios.create({
  baseURL: 'http://deti-tqs-15.ua.pt:8080/api/',
})