from django.shortcuts import get_object_or_404, render, redirect
from django.contrib.auth import authenticate, login, logout
from django.views.generic import ListView, CreateView
from .models import Perfil, Pregunta, Cita
from django.views import View

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
        return redirect('login')

class DoctorVirtualView(ListView):
    model = Pregunta
    template_name = 'doctor_virtual.html'
    
    def get_queryset(self):
        perfil = Perfil.objects.get(user=self.request.user)
        if perfil.tipo_usuario == 'DOC':
            return Pregunta.objects.filter(medico=None)
        else:
            return Pregunta.objects.filter(perfil=perfil)

class CitasView(ListView):
    model = Cita
    template_name = 'citas.html'
    
    def get_queryset(self):
        perfil = Perfil.objects.get(user=self.request.user)
        if perfil.tipo_usuario == 'DOC':
            return Cita.objects.filter(medico=perfil)
        else:
            return Cita.objects.filter(paciente=perfil)
     
class ResponderPreguntaView(CreateView):
    model = Pregunta
    fields = ['respuesta']
    template_name = 'responder_pregunta.html'
    
    def form_valid(self, form):
        form.instance.medico = Perfil.objects.get(user=self.request.user)
        return super().form_valid(form)

class NuevaPreguntaView(CreateView):
    model = Pregunta
    fields = ['titulo', 'descripcion']
    template_name = 'nueva_pregunta.html'
    
    def form_valid(self, form):
        form.instance.perfil = Perfil.objects.get(user=self.request.user)
        return super().form_valid(form)

class NuevaCitaView(CreateView):
    model = Cita
    fields = ['fecha', 'hora', 'especialidad', 'medico']
    template_name = 'nueva_cita.html'
    
    def form_valid(self, form):
        form.instance.paciente = Perfil.objects.get(user=self.request.user)
        return super().form_valid(form)
    
class CancelarCitaView(View):
    def post(self, request, pk):
        cita = get_object_or_404(Cita, pk=pk)
        if Perfil.objects.get(user=request.user) == cita.paciente:
            cita.cancelada = True
            cita.save()
            return redirect('citas')  
        else:
            return render(request, 'error.html', {'message': 'No tienes permiso para cancelar esta cita.'})
