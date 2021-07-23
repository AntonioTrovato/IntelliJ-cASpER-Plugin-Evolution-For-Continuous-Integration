package it.unisa.casper.adapters;

import com.intellij.psi.PsiImportStatement;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.*;

public class IImportDeclarationAdapter implements IImportDeclaration {

    public IImportDeclarationAdapter(PsiImportStatement psiImportStatement) {
        this.psiImportStatement = psiImportStatement;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public String getSource() throws JavaModelException {
        return null;
    }

    @Override
    public ISourceRange getSourceRange() throws JavaModelException {
        return null;
    }

    @Override
    public ISourceRange getNameRange() throws JavaModelException {
        return null;
    }

    @Override
    public IJavaElement getAncestor(int i) {
        return null;
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor iProgressMonitor) throws JavaModelException {
        return null;
    }

    @Override
    public IResource getCorrespondingResource() throws JavaModelException {
        return null;
    }

    @Override
    public String getElementName() {
        return psiImportStatement.getQualifiedName();
    }

    @Override
    public int getElementType() {
        return 0;
    }

    @Override
    public String getHandleIdentifier() {
        return null;
    }

    @Override
    public IJavaModel getJavaModel() {
        return null;
    }

    @Override
    public IJavaProject getJavaProject() {
        return null;
    }

    @Override
    public IOpenable getOpenable() {
        return null;
    }

    @Override
    public IJavaElement getParent() {
        return null;
    }

    @Override
    public IPath getPath() {
        return null;
    }

    @Override
    public IJavaElement getPrimaryElement() {
        return null;
    }

    @Override
    public IResource getResource() {
        return null;
    }

    @Override
    public ISchedulingRule getSchedulingRule() {
        return null;
    }

    @Override
    public IResource getUnderlyingResource() throws JavaModelException {
        return null;
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isStructureKnown() throws JavaModelException {
        return false;
    }

    @Override
    public int getFlags() throws JavaModelException {
        return 0;
    }

    @Override
    public boolean isOnDemand() {
        return false;
    }

    @Override
    public Object getAdapter(Class aClass) {
        return null;
    }

    @Override
    public void copy(IJavaElement iJavaElement, IJavaElement iJavaElement1, String s, boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void delete(boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void move(IJavaElement iJavaElement, IJavaElement iJavaElement1, String s, boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    @Override
    public void rename(String s, boolean b, IProgressMonitor iProgressMonitor) throws JavaModelException {

    }

    private PsiImportStatement psiImportStatement;
}
