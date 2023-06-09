from django.http import HttpResponseRedirect
from django.shortcuts import render, redirect
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.mixins import LoginRequiredMixin
from django.urls import reverse, reverse_lazy
from django.views import View
from django.utils import timezone 
from django.views.generic import ListView, CreateView, UpdateView
from .models import Perfil, Pregunta, Cita

class LoginView(View):
    def get(self, request):
        return render(request, 'login.html')

    def post(self, request):
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(request, username=username, password=password)
        if user is not None:
            login(request, user)
            return redirect('clinic:citas')
        else:
            return render(request, 'login.html', {'error': 'Usuario o contraseña incorrectos'})

class LogoutView(View):
    def get(self, request):
        logout(request)
        return HttpResponseRedirect(reverse('clinic:login'))

class DoctorVirtualView(LoginRequiredMixin,ListView):
    model = Pregunta
    template_name = 'doctor_virtual.html'
    
    def get_queryset(self):
        perfil = Perfil.objects.get(user=self.request.user)
        if perfil.tipo_usuario == 'DOC':
            return Pregunta.objects.filter(medico=perfil, respuesta='')
        else:
            return Pregunta.objects.filter(perfil=perfil)

class CitasView(LoginRequiredMixin, ListView):
    model = Cita
    template_name = 'citas.html'
    
    def get_queryset(self):
        perfil = Perfil.objects.get(user=self.request.user)
        if perfil.tipo_usuario == 'DOC':
            today = timezone.localdate()
            # Filtra las citas para que solo las del dia actual
            return Cita.objects.filter(medico=perfil, fecha=today)
        else:
            return Cita.objects.filter(paciente=perfil)
     
class ResponderPreguntaView(LoginRequiredMixin, UpdateView):
    model = Pregunta
    fields = ['respuesta']
    template_name = 'responder_pregunta.html'
    success_url = reverse_lazy('clinic:doctor_virtual')
    
    def form_valid(self, form):
        form.instance.medico = Perfil.objects.get(user=self.request.user)
        return super().form_valid(form)

class NuevaPreguntaView(CreateView):
    model = Pregunta
    fields = ['titulo', 'descripcion', 'medico']
    template_name = 'nueva_pregunta.html'
    success_url = reverse_lazy('clinic:doctor_virtual')
    
    def form_valid(self, form):
        form.instance.perfil = Perfil.objects.get(user=self.request.user)
        return super().form_valid(form)

    def get_form(self, form_class=None):
        form = super().get_form(form_class)
        form.fields['medico'].queryset = Perfil.objects.filter(tipo_usuario='DOC')
        return form

class NuevaCitaView(CreateView):
    model = Cita
    fields = ['fecha', 'hora', 'medico']
    template_name = 'nueva_cita.html'
    success_url = reverse_lazy('clinic:citas')
    
    def form_valid(self, form):
        form.instance.paciente = Perfil.objects.get(user=self.request.user)
        return super().form_valid(form)

    def get_form(self, form_class=None):
        form = super().get_form(form_class)
        form.fields['medico'].queryset = Perfil.objects.filter(tipo_usuario='DOC')
        return form
    
class CancelarCitaView(UpdateView):
    model = Cita
    fields = ['cancelada']
    template_name = 'cancelar_cita.html'
    success_url = reverse_lazy('clinic:citas')
    
    def form_valid(self, form):
        form.instance.cancelada = True
        return super().form_valid(form)


