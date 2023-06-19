from django.shortcuts import get_object_or_404, render, redirect
from django.contrib.auth import authenticate, login, logout
from django.contrib.auth.mixins import LoginRequiredMixin
from django.views.generic import ListView, CreateView
from .models import Perfil, Pregunta, Cita
from django.views import View

class LoginView(LoginRequiredMixin,View):
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

class LogoutView(LoginRequiredMixin,View):
    def get(self, request):
        logout(request)
        return redirect('login')

class DoctorVirtualView(LoginRequiredMixin,ListView):
    model = Pregunta
    template_name = 'doctor_virtual.html'
    
    def get_queryset(self):
        perfil = Perfil.objects.get(user=self.request.user)
        if perfil.tipo_usuario == 'DOC':
            return Pregunta.objects.filter(medico=None)
        else:
            return Pregunta.objects.filter(perfil=perfil)

class CitasView(LoginRequiredMixin,ListView):
    model = Cita
    template_name = 'citas.html'
    
    def get_queryset(self):
        perfil = Perfil.objects.get(user=self.request.user)
        if perfil.tipo_usuario == 'DOC':
            return Cita.objects.filter(medico=perfil)
        else:
            return Cita.objects.filter(paciente=perfil)
     
class ResponderPreguntaView(LoginRequiredMixin,CreateView):
    model = Pregunta
    fields = ['respuesta']
    template_name = 'responder_pregunta.html'
    
    def form_valid(self, form):
        if form.is_valid():
            form.instance.medico = Perfil.objects.get(user=self.request.user)
            return super().form_valid(form)
        else:
            return render(self.request, 'responder_pregunta.html', {'form': form})

class NuevaPreguntaView(LoginRequiredMixin,CreateView):
    model = Pregunta
    fields = ['titulo', 'descripcion']
    template_name = 'nueva_pregunta.html'
    
    def form_valid(self, form):
        if form.is_valid():
            form.instance.perfil = Perfil.objects.get(user=self.request.user)
            return super().form_valid(form)
        else:
            return render(self.request, 'nueva_pregunta.html', {'form': form})

class NuevaCitaView(LoginRequiredMixin,CreateView):
    model = Cita
    fields = ['fecha', 'hora', 'especialidad', 'medico']
    template_name = 'nueva_cita.html'
    
    def form_valid(self, form):
        form.instance.paciente = Perfil.objects.get(user=self.request.user)
        return super().form_valid(form)
    
class CancelarCitaView(LoginRequiredMixin,View):
    def post(self, request, pk):
        cita = get_object_or_404(Cita, pk=pk)
        if Perfil.objects.get(user=request.user) == cita.paciente:
            cita.cancelada = True
            cita.save()
            return redirect('citas')  
        else:
            return render(request, 'error.html', {'message': 'No tienes permiso para cancelar esta cita.'})
