from django.contrib import admin
from django.urls import include, path, re_path
from django.views.generic.base import RedirectView

urlpatterns = [
    path('admin/', admin.site.urls),
    path('clinic/', include('clinic.urls')),
    re_path(r'^$', RedirectView.as_view(url='/clinic/login/', permanent=False)),

]
